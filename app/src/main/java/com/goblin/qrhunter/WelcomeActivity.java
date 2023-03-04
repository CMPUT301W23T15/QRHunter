package com.goblin.qrhunter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.goblin.qrhunter.data.PlayerRepository;
import com.goblin.qrhunter.databinding.ActivityWelcomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Random;

public class WelcomeActivity extends AppCompatActivity {
    private ActivityWelcomeBinding binding;
    private String TAG = "WelcomeActivity";
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private PlayerRepository playerDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        playerDB = new PlayerRepository();
        signInSetup();


    }

    public void signInSetup() {
        if (auth.getCurrentUser() != null) {
            finish();
        }
        binding.welcomeButton.setOnClickListener(v-> {
            auth.signInAnonymously().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                        playerDB.addWithRandomUsername(task.getResult().getUser().getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                finish();
                            }
                        });
                } else {
                    Log.e(TAG, "signInAnonymously:failure", task.getException());
                }
            });
        });
    }


}
