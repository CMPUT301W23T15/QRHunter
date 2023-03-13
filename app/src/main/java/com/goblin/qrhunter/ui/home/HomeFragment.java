/**
 * A fragment representing the home screen of the app.
 * This screen displays the user's current score and provides options to navigate to other screens.
 * The options include: scanning a QR code, viewing the leaderboard, and viewing a map of the game's locations.
 */
package com.goblin.qrhunter.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.goblin.qrhunter.R;
import com.goblin.qrhunter.databinding.FragmentHomeBinding;

import java.util.HashMap;
import java.util.function.BiConsumer;


/**
 * A {@link Fragment} representing the home screen of the application. This fragment displays
 * buttons for navigating to other screens of the application, such as the map screen,
 * the scan screen, and the leaderboard screen. The buttons are wired up to the appropriate
 * navigation actions using the Android Navigation Component.
 */
public class HomeFragment extends Fragment {

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
        homeViewModel.getUserHighScore().observe(getViewLifecycleOwner(), highScore -> {
           binding.titleHighestScoring.setText(getString(R.string.highest_scoring_title) + highScore);
        });
        homeViewModel.getUserLowScore().observe(getViewLifecycleOwner(), lowScore -> {
            binding.titleLowestScoring.setText(getString(R.string.lowest_scoring_title) + lowScore);
        });
        homeViewModel.getUserQRCount().observe(getViewLifecycleOwner(), qrCount -> {
            binding.titleTotalQRCode.setText(getString(R.string.total_qr_codes_scanned_title) + qrCount);
        });
        homeViewModel.getUserTotalScore().observe(getViewLifecycleOwner(), totalScore -> {
            binding.titleTotalScore.setText(getString(R.string.total_score_title) + totalScore);
        });



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

}