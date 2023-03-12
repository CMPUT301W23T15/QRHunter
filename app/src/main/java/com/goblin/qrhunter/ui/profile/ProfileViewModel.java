/**
 * Contains the classes responsible for managing the UI and data associated with the ProfileFragment.
 *
 * The ProfileFragment displays the user's profile information, such as their username
 */
package com.goblin.qrhunter.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.goblin.qrhunter.data.PlayerRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * ViewModel for the ProfileFragment.
 *
 * This class is responsible for managing the data that is displayed on the ProfileFragment.
 * It gets the currently logged in user's player information from the PlayerRepository and
 * updates the MutableLiveData for the username, which is observed by the ProfileFragment
 * to update the UI.
 */
public class ProfileViewModel extends ViewModel {
    MutableLiveData<String> username = new MutableLiveData<>();
    PlayerRepository playerDB;
    /**
     * Constructs a new ProfileViewModel and initializes the LiveData to hold the user's username.
     * If the user is logged in, the username is retrieved from the Firebase Realtime Database,
     * otherwise the LiveData remains empty.
     */
    public ProfileViewModel() {
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
     *
     * @return LiveData object that holds the user's username.
     */
    public LiveData<String> getUsername() {
        return  username;
    }
}