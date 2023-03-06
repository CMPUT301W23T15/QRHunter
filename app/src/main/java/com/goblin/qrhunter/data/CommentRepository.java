package com.goblin.qrhunter.data;


import com.goblin.qrhunter.Comment;


public class CommentRepository extends BaseRepository<Comment> {
    public CommentRepository() {
        super("comments", Comment.class);
    }
}
