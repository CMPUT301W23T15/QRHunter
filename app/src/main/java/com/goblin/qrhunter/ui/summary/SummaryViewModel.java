/**
 * SummaryViewModel Stub
 */
package com.goblin.qrhunter.ui.summary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.data.PlayerRepository;
import com.goblin.qrhunter.data.PostRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * SummaryViewModel Stub
 */
public class SummaryViewModel extends ViewModel {

    MutableLiveData<String> username = new MutableLiveData<>();
    MediatorLiveData<List<Post>> userPosts = new MediatorLiveData<>();
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

        userPosts.addSource(postDB.getUserPosts(playerId), userPosts::setValue);
        playerDB.get(playerId).addOnSuccessListener(player -> {username.setValue(player.getUsername());});
    }

    /**
     * Returns a LiveData object that holds the user's username.
     * @return LiveData object that holds the user's username.
     */
    public LiveData<String> getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username.setValue(username);
    }

    /**
     * get all of the post from the current user
     * @return livedata list of posts
     */
    public MediatorLiveData<List<Post>> getUserPosts() {
        return userPosts;
    }

    public void setUserPosts(List<Post> lp) {
        this.userPosts.setValue(lp);
    }
}