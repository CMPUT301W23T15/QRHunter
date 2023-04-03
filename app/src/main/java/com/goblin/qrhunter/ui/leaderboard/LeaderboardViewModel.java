/**
 * The com.goblin.qrhunter.ui.leaderboard package contains classes related to the leaderboard UI.
 *
 * The LeaderboardViewModel class is a ViewModel class that provides data to the LeaderboardFragment.
 * It uses a MediatorLiveData object to observe changes to a LiveData object that holds a list of Post
 * objects retrieved from the PostRepository. This list is then provided to the UI as a LiveData object
 * using the getPostList() method.
 */
package com.goblin.qrhunter.ui.leaderboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.Score;
import com.goblin.qrhunter.data.PostRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * ViewModel class for the leaderboard that retrieves the list of posts from the repository
 * and provides it to the view.
 */
public class LeaderboardViewModel extends ViewModel {
    String TAG = "leaderboardViewModel";
    final private PostRepository postDB;
    private LiveData<List<Post>> postSource;
    final private MediatorLiveData<List<Post>> topPostList = new MediatorLiveData<>();
    
    private LiveData<Score> scoreLiveData;
//    private List<Post> seedList = new ArrayList<>();

    /**
     * Constructs a new instance of LeaderboardViewModel and initializes a list of top posts sorted
     * by score
     */
    public LeaderboardViewModel() {
        postDB = new PostRepository();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null) {
            postSource = postDB.getPostByPlayer(user.getUid());
        } else {

            postSource=new MutableLiveData<>(new ArrayList<>());
        }
        scoreLiveData = postDB.getScoreByPlayerId(user.getUid());

        postSource = postDB.getAll();
        topPostList.addSource(postSource, posts -> {
            // Sort posts by QRCode score
            Collections.sort(posts, Comparator.comparingInt(p -> p.getCode().getScore()));
            topPostList.setValue(posts);
        });
    }

    public MediatorLiveData<List<Post>> getTopPosts() {
        return topPostList;
    }
    public LiveData<Score> getScore() {
        return scoreLiveData;
    }

}