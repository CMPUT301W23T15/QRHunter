/**
 * The WelcomeActivity displays a welcome screen and allows the user to sign in anonymously using Firebase Authentication.
 * If the user is already signed in, the activity will finish and redirect to the previous activity.
 * The activity initializes the Firebase Authentication instance and sets up the sign-in button to sign in anonymously.
 * The activity uses a PlayerRepository object to add a new player with a random username to the database after a successful sign-in.
 */
package com.goblin.qrhunter.ui.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.goblin.qrhunter.MainActivity;
import com.goblin.qrhunter.data.PlayerRepository;
import com.goblin.qrhunter.databinding.ActivityWelcomeBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Activity for welcoming the user to the app and allowing them to sign in anonymously.
 * If the user is already signed in, the activity will finish immediately.
 */
public class WelcomeActivity extends AppCompatActivity {
    private ActivityWelcomeBinding binding;
    private String TAG = "WelcomeActivity";
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private PlayerRepository playerDB;

    /**
     * Initializes the activity and sets the content view using the ActivityWelcomeBinding object.
     * Calls signInSetup() to set up the sign-in button and initialize the PlayerRepository object.
     *
     * @param savedInstanceState the saved state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        playerDB = new PlayerRepository();
        signInSetup();

    }

    /**
     * Sets up the sign-in button to sign in anonymously using Firebase Authentication.
     * If the user is already signed in, finishes the activity.
     * If the sign-in is successful, calls addWithRandomUsername() to add a new player to the database.
     * If the sign-in is unsuccessful, logs the error message.
     */
    public void signInSetup() {
        Intent mainIntent = new Intent(WelcomeActivity.this, MainActivity.class);

        if (auth.getCurrentUser() != null) {
            startActivity(mainIntent);
        }
        binding.welcomeButton.setOnClickListener(v -> {
            auth.signInAnonymously().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    playerDB.addWithRandomUsername(task.getResult().getUser().getUid()).addOnSuccessListener(unused -> {
                        startActivity(mainIntent);
                    });
                } else {
                    Log.e(TAG, "signInAnonymously:failure", task.getException());
                }
            });
        });
    }


}
