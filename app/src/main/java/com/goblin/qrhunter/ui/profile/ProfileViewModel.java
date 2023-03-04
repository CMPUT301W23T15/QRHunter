package com.goblin.qrhunter.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.data.PlayerRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileViewModel extends ViewModel {
    MutableLiveData<String> username = new MutableLiveData<>();
    PlayerRepository playerDB;
    public ProfileViewModel() {
        super();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        playerDB = new PlayerRepository();
        username.setValue("Unknown user");
        if (user != null) {
           playerDB.get(user.getUid()).addOnSuccessListener(player -> username.setValue(player.getUsername()));
        }
    }

    public LiveData<String> getUsername() {
        return  username;
    }
}