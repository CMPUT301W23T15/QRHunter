package com.goblin.qrhunter.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileViewModel extends ViewModel {
    MutableLiveData<String> username = new MutableLiveData<>();
    // TODO: Implement the ViewModel
    public ProfileViewModel() {
        super();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            username.setValue(user.getDisplayName());
        } else {
            username.setValue("Unknown user");
        }
    }

    public LiveData<String> getUsername() {
        return  username;
    }
}