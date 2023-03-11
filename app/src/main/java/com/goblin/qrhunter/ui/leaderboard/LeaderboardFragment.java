package com.goblin.qrhunter.ui.leaderboard;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goblin.qrhunter.R;

/**
 * A simple {@link Fragment} subclass.
 * Displays a leaderboard of players with their scores.
 */
public class LeaderboardFragment extends Fragment {
    private LeaderboardViewModel mViewModel;

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
        return inflater.inflate(R.layout.fragment_leaderboard, container, false);
    }
}
