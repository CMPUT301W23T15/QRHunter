/**
 * The HomeViewModel class is responsible for handling the logic related to the home screen of the QR Hunter app.
 * It manages the user's posts, scores, and QR code count, and communicates with the PostRepository to fetch the required data.
 * The class uses LiveData and MediatorLiveData to observe changes in the data and update the UI accordingly.
 */
package com.goblin.qrhunter.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.data.PostRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;



/**
 * ViewModel class that manages data related to a user's QR codes, including the number of codes,
 * highest score, lowest score, and total score.
 */
public class HomeViewModel extends ViewModel {

    /**
     * Inner class that holds the high score, low score, and total score for a user's QR codes.
     * To reduce the number of loops.
     */
    class Scores {
        int highScore = 0;
        int lowScore = 0;
        int totalScore = 0;
    }

    private PostRepository postDB;
    private final MediatorLiveData<List<Post>> userPosts = new MediatorLiveData<>();
    private final MediatorLiveData<Scores> userScores = new MediatorLiveData<>() {{
        setValue(new Scores());
    }};
    private final MediatorLiveData<Integer> userQRCount = new MediatorLiveData<>();

    /**
     * Constructor for home view model that creates initializes the PostRepository and
     * observes changes in the user's posts.
     */
    public HomeViewModel() {
        postDB = new PostRepository();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userQRCount.addSource(userPosts, posts -> {
                userQRCount.setValue(posts.size());
            });

            // do high, low and total score in one loop
            userScores.addSource(userPosts, posts -> {
                for (Post post : posts) {
                    if (post != null && post.getCode() != null) {
                        int postScore = post.getCode().getScore();
                        Scores current = userScores.getValue();
                        if (current != null) {
                            current.highScore = Math.max(postScore, current.highScore);
                            current.lowScore = Math.min(postScore, current.lowScore);
                            current.totalScore = current.totalScore + postScore;
                        }
                    }
                }
            });

            userPosts.addSource(postDB.getPostByPlayer(user.getUid()), userPosts::setValue);

        }
    }

    /**
     * Returns the MediatorLiveData object that holds the user's QR code count.
     *
     * @return MediatorLiveData object that holds the user's QR code count
     */
    public MediatorLiveData<Integer> getUserQRCount() {
        return userQRCount;
    }

    /**
     * Returns a LiveData object that observes changes in the user's high score and updates the UI.
     *
     * @return LiveData object that observes changes in the user's high score
     */
    public LiveData<Integer> getUserHighScore() {
        MediatorLiveData<Integer> highScore = new MediatorLiveData<>();
        highScore.addSource(userScores, scores -> {highScore.setValue(scores.highScore);});
        return  highScore;
    }

    /**
     * Returns a LiveData object that observes changes in the user's low score and updates the UI.
     *
     * @return LiveData object that observes changes in the user's low score
     */
    public LiveData<Integer> getUserLowScore() {
        MediatorLiveData<Integer> lowScore = new MediatorLiveData<>();
        lowScore.addSource(userScores, scores -> {lowScore.setValue(scores.lowScore);});
        return  lowScore;
    }

    /**
     * Returns a LiveData object that observes changes in the user's total score and updates the UI.
     *
     * @return LiveData object that observes changes in the user's total score
     */
    public LiveData<Integer> getUserTotalScore() {
        MediatorLiveData<Integer> totalScore = new MediatorLiveData<>();
        totalScore.addSource(userScores, scores -> {totalScore.setValue(scores.totalScore);});
        return  totalScore;
    }
}