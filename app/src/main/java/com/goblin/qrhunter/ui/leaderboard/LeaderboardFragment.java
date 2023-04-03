package com.goblin.qrhunter.ui.leaderboard;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goblin.qrhunter.R;
import com.goblin.qrhunter.Score;
import com.goblin.qrhunter.databinding.FragmentHomeBinding;
import com.goblin.qrhunter.databinding.FragmentLeaderboardBinding;
import com.goblin.qrhunter.ui.home.HomeViewModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Displays a leaderboard of players with their scores.
 */
public class LeaderboardFragment extends Fragment {
    private LeaderboardViewModel mViewModel;
    private FragmentLeaderboardBinding binding;


    /**
     * Creates a new instance of the LeaderboardFragment.
     *
     * @return A new instance of LeaderboardFragment.
     */
    public static LeaderboardFragment newInstance() {
        return new LeaderboardFragment();
    }

    /**
     * Inflates the view and returns the root view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI will be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return the inflated view.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment using the FragmentHomeBinding
        // Create a new instance of HomeViewModel
        mViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // Set up for highest scoring QR code

        binding.textViewHighestScore.setText("My Highest Scoring QR Code: 0");
        mViewModel.getScore().observe(getViewLifecycleOwner(), score -> {
                    if (score == null) {
                        score = new Score();
                    }
                    binding.textViewHighestScore.setText("My Highest Scoring QR Code: " + score.getHighestScore());

                });

        binding.titleRanking.setText("My Approximate Ranking: 0");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference scoresRef = db.collection("scores");
        mViewModel.getScore().observe(getViewLifecycleOwner(), score -> {
            if (score == null) {
                score = new Score();
            }
            int highestScore = score.getHighestScore();

            // Query the scores collection to get the count of players with a higher score
            scoresRef.whereGreaterThan("highestScore", highestScore)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        int higherScoreCount = queryDocumentSnapshots.size();
                        int approximateRank = higherScoreCount + 1;
                        binding.titleRanking.setText("My Approximate Ranking: " + approximateRank);
                    });

        });



        NavController navController = Navigation.findNavController(container);

        binding.buttonRankByTotalScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_leaderboardFragment_to_navigation_rankingListTotalscore);
            }
        });

        binding.buttonRankByTotalAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_leaderboardFragment_to_navigation_rankingListTotalAmount);
            }
        });

        return binding.getRoot();
    }
}
