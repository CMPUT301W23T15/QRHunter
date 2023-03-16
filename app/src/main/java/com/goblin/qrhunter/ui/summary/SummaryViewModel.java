/**
 * The SummaryViewModel is responsible for managing and providing data related to the summary screen
 * of the application. Which holds the current users post history.
 */
package com.goblin.qrhunter.ui.summary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.data.PlayerRepository;
import com.goblin.qrhunter.data.PostRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * The SummaryViewModel class is responsible for managing
 * and providing data related to the summary screen of the application.
 */
public class SummaryViewModel extends ViewModel {

    MutableLiveData<String> username = new MutableLiveData<>();
    LiveData<List<Post>> userPosts;

    String playerId;

    PostRepository postDB;
    PlayerRepository playerDB;

    /**
     * Constructs a new SummaryViewModel and initializes the LiveData to hold the user's username.
     * If the user is logged in, the username is retrieved from the Firebase Realtime Database,
     * otherwise the LiveData remains empty.
     */
    public SummaryViewModel() {
        super();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // if user is not logged in they will be kicked back to the welcome screen.
        assert user != null;
        playerId = user.getUid();
        postDB = new PostRepository();
        playerDB = new PlayerRepository();

    }

    private String getUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // if user is not logged in they will be kicked back to the welcome screen.
        assert user != null;
        return user.getUid();
    }

    /**
     * Returns a LiveData object that holds the user's username.
     *
     * @return LiveData object that holds the user's username.
     */
    public LiveData<String> getUsername() {
        playerDB.getPlayerById(getUserId()).addOnSuccessListener(player -> {
            if (player != null) {
                username.setValue(player.getUsername());
            }
        });
        return username;
    }

    /**
     * get all of the post from the current user
     *
     * @return livedata list of posts
     */
    public LiveData<List<Post>> getUserPosts() {
        return postDB.getPostByPlayer(getUserId());
    }
}