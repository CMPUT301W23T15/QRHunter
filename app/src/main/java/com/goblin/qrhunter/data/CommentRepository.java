package com.goblin.qrhunter.data;

import com.goblin.qrhunter.Comment;

public class CommentRepository extends FirestoreRepository<Comment> {
    protected CommentRepository(String collectionPath) {
        super(collectionPath, Comment.class);
    }

    public CommentRepository() {
        super("/comments", Comment.class);
    }
}
