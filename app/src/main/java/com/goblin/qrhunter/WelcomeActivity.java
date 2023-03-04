package com.goblin.qrhunter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.goblin.qrhunter.databinding.ActivityWelcomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        signInSetup();

    }

    public void signInSetup() {
        binding.welcomeButton.setOnClickListener(v-> {
            auth.signInAnonymously().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        String username = user.getDisplayName();
                        if (username == null || username.isEmpty()) {

                            username = "User" + new Random().nextInt(999999999);
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();
                            user.updateProfile(profileUpdates);
                        }
                        finish();
                    }
                } else {
                    Log.e(TAG, "signInAnonymously:failure", task.getException());
                }
            });
        });
    }


}
