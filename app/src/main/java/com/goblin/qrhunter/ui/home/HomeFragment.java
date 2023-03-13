/**
 * A fragment representing the home screen of the app.
 * This screen displays the user's current score and provides options to navigate to other screens.
 * The options include: scanning a QR code, viewing the leaderboard, and viewing a map of the game's locations.
 */
package com.goblin.qrhunter.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.QRCode;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.data.PostRepository;
import com.goblin.qrhunter.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A {@link Fragment} representing the home screen of the application. This fragment displays
 * buttons for navigating to other screens of the application, such as the map screen,
 * the scan screen, and the leaderboard screen. The buttons are wired up to the appropriate
 * navigation actions using the Android Navigation Component.
 */
public class HomeFragment extends Fragment {
    String TAG = "HomeFragment";
    private FragmentHomeBinding binding;

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Create a new instance of HomeViewModel
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Inflate the layout for this fragment using the FragmentHomeBinding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // setup scores and post count
        homeViewModel.getUserPosts().observe(getViewLifecycleOwner(), this::refresh);
        List<Post> seedData = homeViewModel.getSeedList();
        if( seedData != null) {
            this.refresh(seedData);
        }

        // Set up the navigation options as buttons
        NavController navController = Navigation.findNavController(container);

        // Navigate to the MapFragment when the Map button is clicked
        binding.mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_home_to_mapFragment);
            }
        });

        // Navigate to the ScanFragment when the Scan button is clicked
        binding.scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Post p1 = new Post();
                p1.setPlayerId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                p1.setName("hello3");
                p1.setCode(new QRCode("hello world! james"));
                PostRepository postRepository = new PostRepository();
                postRepository.add(p1);
                navController.navigate(R.id.action_navigation_home_to_scanFragment);
            }
        });

        // Navigate to the LeaderboardFragment when the Leaderboard button is clicked
        binding.leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_home_to_leaderboardFragment);
            }
        });

        return root;
    }

    /**
     * This method is called when the fragment is no longer in use.
     * It sets the FragmentHomeBinding to null to prevent memory leaks.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void refresh(List<Post> posts) {
        if (posts == null || posts.isEmpty()) {
            return;
        }
        int highScore = 0;
        int lowScore = -1;
        int totalScore = 0;
        int qrCount = 0;
        for (Post p : posts) {
            for (Post post : posts) {
                if(post.getCode() != null) {
                    int score = post.getCode().getScore();
                    totalScore = totalScore + score;
                    highScore = Math.max(score, highScore);
                    if(lowScore < 0) {
                        lowScore = score;
                    }
                    lowScore = Math.min(score, lowScore);
                    Log.d("Adding", "HomeViewModel: " + post.getId());
                }
                qrCount = posts.size();
            }
        }

        binding.titleHighestScoring.setText("Highest score: " + highScore);
        binding.titleLowestScoring.setText("Lowest score: " + lowScore);
        binding.titleTotalScore.setText("Total score: " + totalScore);
        binding.titleTotalQRCode.setText( "Number of QRcodes: " + qrCount);
    }
}