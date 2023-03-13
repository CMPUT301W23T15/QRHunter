/**
 * A repository class for managing Post objects in Firestore database.
 */
package com.goblin.qrhunter.data;

import androidx.lifecycle.LiveData;

import com.goblin.qrhunter.Post;
import com.google.firebase.firestore.Query;

import java.util.List;

/**
 * A repository class for managing Post objects in a Firestore database.
 * This class provides methods for retrieving, adding, and updating posts.
 */
public class PostRepository extends BaseRepository<Post> {

    public PostRepository() {
        super("posts", Post.class);
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
     * get a livedata list of all the users posts
     * @param id user id
     * @return all user posts
     */
    public LiveData<List<Post>> getUserPosts(String id) {
        Query q = getCollectionRef().whereEqualTo("playerId", id);
        return new FirebaseLiveData<>(q, Post.class);
    }
}