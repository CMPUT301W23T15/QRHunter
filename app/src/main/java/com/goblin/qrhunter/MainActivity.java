/**
 * MainActivity is the main entry point for the application. It is responsible for setting up the
 * UI, initializing Firebase, and checking if the user is signed in or not. If the user is not
 * signed in, the activity will redirect them to the WelcomeActivity. The activity also handles
 * camera functionality for scanning QR codes and navigation to the profile screen via the action
 * bar menu.
 */
package com.goblin.qrhunter;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.goblin.qrhunter.data.PlayerRepository;
import com.goblin.qrhunter.databinding.ActivityMainBinding;
import com.goblin.qrhunter.ui.scan.ScanActivity;
import com.goblin.qrhunter.ui.welcome.WelcomeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.type.LatLng;


/**
 * The main activity of the QR Hunter app, which serves as the entry point for the app.
 * This activity sets up the bottom navigation view, handles navigation, and performs user authentication.
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    FirebaseAuth.AuthStateListener authListener;
    private NavController navController;
    private PlayerRepository playerDB;

    FirebaseAuth auth;
    private final String TAG = "mainactivity";
    private String uid;
    FloatingActionButton button_camera;



    /**
     * Initializes the activity's UI, sets up the bottom navigation view and action bar, initializes
     * Firebase, and sets up the camera functionality for scanning QR codes.
     *
     * @param savedInstanceState A saved instance of the current activity state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_summary, R.id.navigation_search)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        playerDB = new PlayerRepository();
    }



    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     * In this method, we create a button that navigates to the profile fragment when clicked.
     *
     * @param menu actionbar menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        menu.findItem(R.id.profile_button).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                navController.navigate(R.id.action_navigate_to_profile);
                return false;
            }
        });
        return true;
    }


    /**
     * This function is called when the user presses the Up button in the app's action bar.
     * It handles navigation up one level in the app's navigation hierarchy.
     * If the navigation controller cannot navigate up, this function calls the superclass implementation.
     *
     * @return true if navigation up succeeded, false otherwise.
     */
    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onNavigateUp();
    }

    /**
     * Called when the activity is starting. This method checks if the user is signed in,
     * and if not, it directs the user to the welcome screen. Sets up a Firebase Auth State Listener
     * to listen for changes in the user's authentication state, and if the user is signed out,
     * kicks back to welcome screen.
     */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in, if not goto welcome screen
        FirebaseUser currentUser = auth.getCurrentUser();
        Intent welcomeIntent = new Intent(MainActivity.this, WelcomeActivity.class);
        if (currentUser == null || currentUser.getUid().isEmpty()) {
            Log.d(TAG, "onStart: no user");
            startActivity(welcomeIntent);
        }

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user signed out
                    startActivity(welcomeIntent);
                }
            }
        };
        FirebaseAuth.getInstance().addAuthStateListener(authListener);
    }

    /**
     * Called when the activity is no longer visible to the user. This method is called after
     * onPause() and before onDestroy(). It is used to release resources, unregister listeners,
     * and perform any other cleanup activities.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
            authListener = null;
        }
    }


}