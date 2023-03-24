/**
 * The HomeViewModel class is responsible for handling the logic related to the home screen of the QR Hunter app.
 * It manages the user's posts, scores, and QR code count, and communicates with the PostRepository to fetch the required data.
 * The class uses LiveData and MediatorLiveData to observe changes in the data and update the UI accordingly.
 */
package com.goblin.qrhunter.ui.home;


import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.goblin.qrhunter.MainActivity;
import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.Score;
import com.goblin.qrhunter.data.PostRepository;
import com.goblin.qrhunter.ui.welcome.WelcomeActivity;
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

    private LiveData<Score> scoreLiveData;

    LiveData<List<Post>> postSource;

    private List<Post> seedList = new ArrayList<>();

    /**
     * Constructor for home view model that creates initializes the PostRepository and
     * observes changes in the user's posts.
     */
    public HomeViewModel() {
        postDB = new PostRepository();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            postSource = postDB.getPostByPlayer(user.getUid());
        }else{
            postSource=new MutableLiveData<>(new ArrayList<>());
        }
        scoreLiveData = postDB.getScoreByPlayerId(user.getUid());
    }


    public LiveData<Score> getScore() {
        return scoreLiveData;
    }

    /**
     * Gets the seed list of posts for the QR Hunter game.
     *
     * @return List<Post> The seed list of posts.
     */
    public LiveData<List<Post>> getUserPosts() {
        return postSource;
    }

    /**
     * Gets the highest score of the user's QR codes.
     *
     * @return int The highest score of the user's QR codes.
     */
    public List<Post> getSeedList() {
        return seedList;
    }

    /**
     * Gets the lowest score of the user's QR codes.
     *
     * @return int The lowest score of the user's QR codes.
     */
    public int getHighScore() {
        return highScore;
    }

    /**
     * Gets the total score of the user's QR codes.
     *
     * @return int The total score of the user's QR codes.
     */
    public int getLowScore() {
        return lowScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Gets the number of QR codes scanned by the user.
     *
     * @return int The number of QR codes scanned by the user.
     */
    public int getQrCount() {
        return postSource.getValue().size();
    }

}