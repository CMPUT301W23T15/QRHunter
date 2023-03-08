package com.goblin.qrhunter.data;

import androidx.lifecycle.LiveData;

import com.goblin.qrhunter.Post;
import com.google.firebase.firestore.Query;

import java.util.List;

public class PostRepository extends BaseRepository<Post> {

    public PostRepository() {
        super("posts", Post.class);
    }

    public LiveData<List<Post>> getTopPosts(int limit) {

        Query q = getCollectionRef().orderBy("code.score", Query.Direction.DESCENDING).limit(limit);
        return new FirebaseLiveData<>(q, Post.class);
    }
}