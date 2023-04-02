package com.goblin.qrhunter.ui.post;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.goblin.qrhunter.Comment;
import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.data.CommentRepository;
import com.goblin.qrhunter.data.PostRepository;

import java.util.List;

public class PostViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private Post post;
    private CommentRepository commentDB;
    public PostViewModel() {
        commentDB = new CommentRepository();
    }

    /**
     * Getter to retrieve a list of comments.
     * @param postId
     * @return Livedata list of comments.
     */
    public LiveData<List<Comment>> getComments(String postId) {
        return commentDB.getByPost(postId);
    }
}