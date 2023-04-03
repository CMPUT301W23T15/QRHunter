package com.goblin.qrhunter.ui.post;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.goblin.qrhunter.Comment;
import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.data.CommentRepository;
import com.goblin.qrhunter.data.PostRepository;

import java.util.List;

/**
 * This class is responsible for holding and processing data for the map screen.
 * It provides access to nearby posts to display on the map.
 */
public class PostViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private Post post;
    private CommentRepository commentDB;

    /**
     * Constructs a new MapViewModel
     */
    public PostViewModel() {
        commentDB = new CommentRepository();
    }

    /**
     * Getter to retrieve a list of comments based on userId
     * @param postId
     * @return Livedata list of comments.
     */
    public LiveData<List<Comment>> getComments(String postId) {
        return commentDB.getByPost(postId);
    }
}