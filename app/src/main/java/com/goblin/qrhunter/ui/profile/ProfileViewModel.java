/**
 * Contains the classes responsible for managing the UI and data associated with the ProfileFragment.
 * <p>
 * The ProfileFragment displays the user's profile information, such as their username
 */
package com.goblin.qrhunter.ui.profile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.data.PlayerRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * ViewModel for the ProfileFragment.
 *
 * This class is responsible for managing the data that is displayed on the ProfileFragment.
 * It gets the currently logged in user's player information from the PlayerRepository and
 * updates the MutableLiveData for the username, which is observed by the ProfileFragment
 * to update the UI.
 */
public class ProfileViewModel extends ViewModel {
    String username = "";
    String phoneNumber = "";
    String email = "";
    LiveData<Player> livePlayer;

    private String TAG="ProfileViewModel";
    PlayerRepository playerDB;

    /**
     * Constructs a new ProfileViewModel and initializes the LiveData to hold the user's username.
     * If the user is logged in, the username is retrieved from the Firebase Realtime Database,
     * otherwise the LiveData remains empty.
     */
    public ProfileViewModel() {
        super();
        Log.d(TAG, "ProfileViewModel: started");
        playerDB = new PlayerRepository();
        Log.d(TAG, "getLivePlayer: called");

    }

    /**
     * Gets the LiveData of the user's player information from the PlayerRepository.
     * Updates the MutableLiveData for the username, email, and phone number,
     * which are observed by the ProfileFragment to update the UI.
     *
     * @return LiveData<Player> The LiveData of the user's player information.
     */
    public LiveData<Player> getLivePlayer() {
        String userId = getUserId();
        LiveData<Player> livePlayer = playerDB.getAsLiveData(userId);
        Player p1 = livePlayer.getValue();
        if(p1 != null) {
            username = p1.getUsername();
            email = p1.getContactInfo();
            phoneNumber = p1.getPhone();
        }
        return playerDB.getAsLiveData(userId);
    }

    /**
     * Updates the player information in the PlayerRepository.
     *
     * @param updated The updated Player object.
     * @return Task<Void> A task that indicates whether the update was successful.
     */
    Task<Void> updatePlayer(Player updated) {
        return playerDB.update(updated);
    }

    /**
     * Gets the user ID of the currently logged in user.
     *
     * @return String The user ID of the currently logged in user.
     */
    private String getUserId() {
        FirebaseUser  usr = FirebaseAuth.getInstance().getCurrentUser();
        if(usr != null) {
            return usr.getUid();
        }
        return null;
    }


}