/**
 * A repository class for managing Post objects in Firestore database.
 */
package com.goblin.qrhunter.data;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.QRCode;
import com.goblin.qrhunter.Score;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A repository class for managing Post objects in a Firestore database.
 * This class provides methods for retrieving, adding, and updating posts.
 */
public class PostRepository extends BaseRepository<Post> {
    String TAG = "PostRepository";
    private final FirebaseFirestore db;
    private final CollectionReference scoreCol;

    /**
     * Constructs a new PostRepository object.
     */
    public PostRepository() {

        super("posts", Post.class);
        this.db = FirebaseFirestore.getInstance();
        scoreCol = this.db.collection("scores");

    }


    /**
     * get a livedata live of the top scoring posts of all time
     *
     * @param limit number of posts to return
     * @return highest scoring posts
     */
    public LiveData<List<Post>> getTopPosts(int limit) {

        Query q = getCollectionRef().orderBy("code.score", Query.Direction.DESCENDING).limit(limit);
        return new FirebaseLiveData<>(q, Post.class);
    }

    /**
     * Returns a LiveData<List<Post>> object containing all the posts created by a player.
     *
     * @param id the id of the player whose posts are to be fetched
     * @return a LiveData<List<Post>> object containing all the posts associated with the specified player id
     */
    public LiveData<List<Post>> getPostByPlayer(String id) {
        return this.getWhereEqualTo("playerId", id);
    }

    /**
     * Adds the given post to the collection.
     *
     * @param post The model object to add to the collection.
     * @return A task indicating whether the operation was successful.
     */
    @Override
    public Task<Void> add(@NonNull Post post) {

        if (post.getCode() == null
                || post.getCode().getHash() == null
                || post.getPlayerId() == null
                || post.getPlayerId().isEmpty()
        ) {
            return Tasks.forException(new IllegalArgumentException("invalid qrcode"));
        }

        List<Post> existsList = this.getWhereEqualTo("postKey", post).getValue();
        if (existsList != null && !existsList.isEmpty()) {
            post.setId(existsList.get(0).getId());
        }

        DocumentReference postRef;
        if (post.getId() == null && post.getId().isEmpty()) {
            postRef = getCollectionRef().document();
            String id = postRef.getId();
            post.setId(id);
        } else {
            postRef = getCollectionRef().document(post.getId());
        }

        DocumentReference scoreRef = getScoreCollection().document(post.getPlayerId());
        DocumentReference playerRef = this.db.collection("players").document(post.getPlayerId());
        QRCode qrCode = post.getCode();

        return db.runTransaction(transaction -> {
            DocumentSnapshot scoreSnapshot = transaction.get(scoreRef);
            DocumentSnapshot playerSnapshot = transaction.get(playerRef);
            Score score;
            if (scoreSnapshot.exists()) {
                score = scoreSnapshot.toObject(Score.class);
                int totalScore = score.getTotalScore();
                totalScore += qrCode.getScore();

                List<QRCode> posts = score.getPosts();
                posts.add(qrCode);
                score.setPosts(posts);
            } else {
                score = new Score();

                List<QRCode> posts = new ArrayList<>();
                posts.add(qrCode);
                score.setPosts(posts);
            }

            Player player = playerSnapshot.toObject(Player.class);
            score.setPlayerId(post.getPlayerId());
            score.setPlayer(player);
            transaction.set(postRef, post.toMap());
            transaction.set(scoreRef, score.toMap());

            return null;
        });
    }

    /**
     * Deletes the post from the collection.
     * @param post object to delete.
     * @return A task indicating whether the operation was successful.
     */
    public Task<Void> delete(@NonNull Post post) {
        if (post.getId() == null || post.getId().isEmpty()) {
            return Tasks.forException(new IllegalArgumentException("invalid post id"));
        }

        DocumentReference postRef = getCollectionRef().document(post.getId());
        DocumentReference scoreRef = getScoreCollection().document(post.getPlayerId());

        return db.runTransaction(transaction -> {
            // Get the current Score document and remove the post ID from its 'posts' list
            Score score = transaction.get(scoreRef).toObject(Score.class);
            if (score != null) {
                score.removeQRCode(post.getCode());
                transaction.set(scoreRef, score.toMap());
            }

            // Delete the post document
            transaction.delete(postRef);

            return null;
        });
    }

    /**
     * Returns a LiveData<List<Score>> object containing all the scores associated with a particular QR code hash.
     * scores contains player info and an array of qrcodes;
     * @param hash the hash of the QR code to look up
     * @return a LiveData<List<Score>> object containing all the scores associated with the specified QR code hash
     */
    public  LiveData<List<Score>> getScoreByQR(String hash) {
        QRCode tmpCode = new QRCode();
        tmpCode.setHash(hash);
        Query query = getScoreCollection().whereArrayContains("posts", tmpCode);
        return new FirebaseLiveData<>(query, Score.class);
    }

    /**
     * Returns a LiveData<Score> object containing the score associated with a particular player ID.
     *
     * @param playerId the ID of the player whose score is to be looked up
     * @return a LiveData<Score> object containing the score associated with the specified player ID
     */
    public LiveData<Score> getScoreByPlayerId(String playerId) {
        DocumentReference scoreRef = getScoreCollection().document(playerId);
        return new FirebaseLiveEntity<>(scoreRef, Score.class);

    }

    public LiveData<List<Post>> getNearBy(double lat, double lng, double radiusInM) {

        Log.d(TAG, "getNearBy: started call");
        MutableLiveData<List<Post>> liveNearbyPost = new MutableLiveData<>();
        List<Post> postList = new ArrayList<>();
        // Find cities within 50km of London
        final GeoLocation center = new GeoLocation(lat, lng);
        if(radiusInM < 1) {
            radiusInM = 50 * 1000;
        }

// Each item in 'bounds' represents a startAt/endAt pair. We have to issue
// a separate query for each pair. There can be up to 9 pairs of bounds
// depending on overlap, but in most cases there are 4.
        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM);
        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (GeoQueryBounds b : bounds) {
            Query q = getCollectionRef()
                    .orderBy("geohash")
                    .startAt(b.startHash)
                    .endAt(b.endHash);

            tasks.add(q.get());
        }

// Collect all the query results together into a single list
        double finalRadiusInM = radiusInM;
        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(t -> {
                    List<DocumentSnapshot> matchingDocs = new ArrayList<>();
                    Log.d(TAG, "getNearBy: oncomplete listener firing");
                    for (Task<QuerySnapshot> task : tasks) {
                        QuerySnapshot snap = task.getResult();
                        for (DocumentSnapshot doc : snap.getDocuments()) {
                            if(doc == null || doc.getDouble("lat") == null || doc.getDouble("lng") == null) {
                                continue;
                            }
                            assert doc != null;
                            double lat1 = doc.getDouble("lat");
                            double lng1 = doc.getDouble("lng");

                            // We have to filter out a few false positives due to GeoHash
                            // accuracy, but most will match
                            GeoLocation docLocation = new GeoLocation(lat1, lng1);
                            double distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center);
                            if (distanceInM <= finalRadiusInM) {
                                Log.d(TAG, "getNearBy: not filtered adding");
                                matchingDocs.add(doc);
                                Post post = doc.toObject(Post.class);
                                if(post != null) {
                                    postList.add(post);
                                }
                            }

                        }
                    }
                    liveNearbyPost.postValue(postList);
                    // matchingDocs contains the results
                    // ...
                });
        return liveNearbyPost;
    }

    /**
     * Returns a reference to the collection containing scores.
     *
     * @return a reference to the collection containing scores
     */
    private CollectionReference getScoreCollection() {
        return scoreCol;
    }


}