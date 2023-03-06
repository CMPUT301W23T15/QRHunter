package com.goblin.qrhunter.ui.leaderboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.data.PostRepository;

import java.util.List;

public class LeaderboardViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private PostRepository postDB;
    public LeaderboardViewModel() {
        postDB = new PostRepository();
    }

    public LiveData<List<Post>> getTopPosts(int limit) {
        return postDB.getTopPosts(limit);
    }
}