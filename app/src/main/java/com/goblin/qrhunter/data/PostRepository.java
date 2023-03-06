package com.goblin.qrhunter.data;

import com.goblin.qrhunter.Post;

public class PostRepository extends BaseRepository<Post> {

    protected PostRepository() {
        super("posts", Post.class);
    }


}