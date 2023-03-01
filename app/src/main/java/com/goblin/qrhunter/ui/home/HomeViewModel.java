package com.goblin.qrhunter.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.data.PlayerRepository;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        PlayerRepository players = new PlayerRepository();
        Player p1 = new Player("user1", "example@email.com");
    }

    public LiveData<String> getText() {
        return mText;
    }
}