/**
 * The HomeViewModel class is responsible for handling the logic related to the home screen of the QR Hunter app.
 * It manages the user's posts, scores, and QR code count, and communicates with the PostRepository to fetch the required data.
 * The class uses LiveData and MediatorLiveData to observe changes in the data and update the UI accordingly.
 */
package com.goblin.qrhunter.ui.home;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.data.PostRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * ViewModel class that manages data related to a user's QR codes, including the number of codes,
 * highest score, lowest score, and total score.
 */
public class HomeViewModel extends ViewModel {

    String TAG = "homeviewmodel";
    private PostRepository postDB;
    private int highScore = 0;
    private int lowScore = 0;
    private int totalScore = 0;
    private int qrCount = 0;

    LiveData<List<Post>> postSource;

    private List<Post> seedList = new ArrayList<>();

    /**
     * Constructor for home view model that creates initializes the PostRepository and
     * observes changes in the user's posts.
     */
    public HomeViewModel() {
        postDB = new PostRepository();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        postSource = postDB.getPostByPlayer(user.getUid());
    }

    public LiveData<List<Post>> getUserPosts() {
        return postSource;
    }

    public List<Post> getSeedList() {
        return seedList;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getLowScore() {
        return lowScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getQrCount() {
        return postSource.getValue().size();
    }

}