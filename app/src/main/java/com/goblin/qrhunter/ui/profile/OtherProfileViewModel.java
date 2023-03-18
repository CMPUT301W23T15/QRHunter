/**
 * The OtherProfileViewModel class is responsible for holding and processing data for the
 * Other users profile
 */
package com.goblin.qrhunter.ui.profile;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.data.PlayerRepository;
import com.goblin.qrhunter.data.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * The OtherProfileViewModel class is responsible for holding and processing data for the OtherProfileFragment.
 * It provides access to player's data and their posts to display on the OtherProfileFragment.
 */
public class OtherProfileViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private String TAG = "OtherProfileViewModel";
    private PostRepository postDB;
    private PlayerRepository playerDB;
    private Player selectedPlayer = new Player();
    private MediatorLiveData<List<Post>> playerPosts = new MediatorLiveData<>();
    private LiveData<List<Post>> postSource = new MutableLiveData<>();

    /**
     * Constructs a new OtherProfileViewModel and initializes the PostRepository and PlayerRepository.
     */
    public OtherProfileViewModel() {
        postDB = new PostRepository();
        playerDB = new PlayerRepository();
        playerPosts.addSource(postSource, this::observer);

    }

    /**
     * Retrieves a player's data by their username and updates the playerPosts MediatorLiveData with the player's posts.
     * used because of weird firestoreui.
     *
     * @param username the player's username
     * @throws ExecutionException
z     */
    public void getPlayerByUsername(@NonNull String username) throws ExecutionException, InterruptedException {
        if (!username.equals(selectedPlayer.getUsername())) {
            playerDB.getPlayerByUsername(username).addOnSuccessListener(player -> {
                Log.d(TAG, "getPlayerByUsername: " + username);
                playerPosts.removeSource(postSource);
                postSource = postDB.getPostByPlayer(player.getId());
                playerPosts.addSource(postSource, this::observer);
            });
        }
    }

    /**
     * Returns a LiveData object that holds a list of posts for the player.
     *
     * @return LiveData object that holds a list of posts for the player.
     */
    public LiveData<List<Post>> getPlayerPosts() {
        return playerPosts;
    }


    private void observer(List<Post> posts) {
        if (posts != null) {
            Log.d(TAG, "observer: adding posts");
            ArrayList<Post> postList = new ArrayList<>(posts.size());
            postList.addAll(posts);
            playerPosts.setValue(postList);
        }
    }


}