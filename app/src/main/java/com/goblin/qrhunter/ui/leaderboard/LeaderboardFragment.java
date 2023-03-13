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
import com.goblin.qrhunter.databinding.FragmentHomeBinding;
import com.goblin.qrhunter.databinding.FragmentLeaderboardBinding;
import com.goblin.qrhunter.ui.home.HomeViewModel;

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
        mViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        NavController navController = Navigation.findNavController(container);

        binding.buttonRankByTotalScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_leaderboardFragment_to_navigation_rankingListTotalscore);
            }
        });

        return binding.getRoot();
    }
}
