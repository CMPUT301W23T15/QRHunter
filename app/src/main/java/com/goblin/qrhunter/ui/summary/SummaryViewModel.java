/**
 * SummaryViewModel Stub
 */
package com.goblin.qrhunter.ui.summary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.goblin.qrhunter.data.PlayerRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * SummaryViewModel Stub
 */
public class SummaryViewModel extends ViewModel {

    MutableLiveData<String> username = new MutableLiveData<>();
    PlayerRepository playerDB;
    /**
     * Constructs a new SummaryViewModel and initializes the LiveData to hold the user's username.
     * If the user is logged in, the username is retrieved from the Firebase Realtime Database,
     * otherwise the LiveData remains empty.
     */
    public SummaryViewModel() {
        super();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        playerDB = new PlayerRepository();
        // create unknown player fallback
        if (user != null) {
            playerDB.get(user.getUid()).addOnSuccessListener(player -> username.setValue(player.getUsername()));
        }
    }

    /**
     * Returns a LiveData object that holds the user's username.
     * @return LiveData object that holds the user's username.
     */
    public LiveData<String> getUsername() {
        return  username;
    }
}