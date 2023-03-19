/**
 * A repository class for managing Post objects in Firestore database.
 */
package com.goblin.qrhunter.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.nio.file.FileAlreadyExistsException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A repository class for managing Post objects in a Firestore database.
 * This class provides methods for retrieving, adding, and updating posts.
 */
public class PostRepository extends BaseRepository<Post> {
    String TAG = "PostRepository";
    private final FirebaseFirestore db;
    private final CollectionReference scoreCollection;


    public PostRepository() {

        super("posts", Post.class);
        this.db = FirebaseFirestore.getInstance();
        scoreCollection = this.db.collection("scores");

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
     * @param post The model object to add to the collection.
     * @return A task indicating whether the operation was successful.
     */
    @Override
    public Task<Void> add(@NonNull Post post) {
        if(post.getCode() == null || post.getCode().getHash() == null) {
            return Tasks.forException(new IllegalArgumentException("invalid qrcode"));
        }
        List<Post> existsList = this.getWhereEqualTo("postKey", post).getValue();
        if(existsList != null && !existsList.isEmpty()) {
            post.setId(existsList.get(0).getId());
        }

        if(post.getId() == null && post.getId().isEmpty()) {
            String id = getCollectionRef().document().getId();
            post.setId(id);
        }

        PlayerRepository playerDB = new PlayerRepository();


        return super.add(post).continueWithTask(task -> {
            Player p1 = new Player();
            p1.setId(post.getPlayerId());
            Map<String, Object>  map1 = new HashMap<String, Object>() {{
                put("hash", post.getCode().getHash());
                put("score", post.getCode().getScore());
                put("postId", post.getId());
            }};
            DocumentReference docRef = playerDB.getCollectionRef().document(post.getPlayerId());

            return docRef.update("posts", FieldValue.arrayUnion(map1));
        });
    }



}