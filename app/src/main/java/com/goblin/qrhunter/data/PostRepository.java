/**
 * A repository class for managing Post objects in Firestore database.
 */
package com.goblin.qrhunter.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.QRCode;
import com.goblin.qrhunter.Score;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

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

    /**
     * Returns a reference to the collection containing scores.
     *
     * @return a reference to the collection containing scores
     */
    private CollectionReference getScoreCollection() {
        return scoreCol;
    }


}