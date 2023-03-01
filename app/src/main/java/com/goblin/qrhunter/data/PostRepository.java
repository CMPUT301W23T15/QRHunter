package com.goblin.qrhunter.data;

import com.goblin.qrhunter.Post;

public class PostRepository extends FirestoreRepository<Post>{

    protected PostRepository(String collectionPath) {
        super(collectionPath, Post.class);
    }

    public PostRepository() {
        super("/posts", Post.class);
    }
}
