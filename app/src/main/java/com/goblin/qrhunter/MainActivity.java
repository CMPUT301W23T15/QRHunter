package com.goblin.qrhunter;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.goblin.qrhunter.data.PlayerRepository;
import com.goblin.qrhunter.ui.welcome.WelcomeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.goblin.qrhunter.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    FirebaseAuth.AuthStateListener authListener;
    private NavController navController;
    private PlayerRepository playerDB;

    FirebaseAuth auth;
    private final String TAG = "main activity";
    private String uid;
    FloatingActionButton button_camera;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 123;



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
        initCamera();


    }

    private void initCamera() {
        button_camera=findViewById(R.id.scan_button);
        button_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check for camera permission
//                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                    // Permission is not granted. Request the permission
//                    ActivityCompat.requestPermissions(MainActivity.this,
//                            new String[]{Manifest.permission.CAMERA},
//                            MY_PERMISSIONS_REQUEST_CAMERA);
//                } else {
//                    // Launch the camera to scan the QR code
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
//                }
                // * moved to ScanActivity *
                Intent scanIntent = new Intent(MainActivity.this, ScanActivity.class);
                button_camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(scanIntent);
                    }
                });
            }
        });
    }


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


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onNavigateUp();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in, if not goto welcome screen
        FirebaseUser currentUser = auth.getCurrentUser();
        Intent welcomeIntent = new Intent(MainActivity.this, WelcomeActivity.class);

        if(currentUser == null){
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

                /* trying to update firebaseauth with username from firestore
                else {
                    if (user.getDisplayName() == null || user.getDisplayName().length() < 1) {
                        playerDB.get(user.getUid()).addOnSuccessListener(new OnSuccessListener<Player>() {
                            @Override
                            public void onSuccess(Player player) {

                            }
                        });
                    }
                }
                */
            }
        };
        FirebaseAuth.getInstance().addAuthStateListener(authListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authListener != null) {
            auth.removeAuthStateListener(authListener);
            authListener = null;
        }
    }


}