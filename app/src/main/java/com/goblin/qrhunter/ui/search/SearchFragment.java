/**
 * The SearchFragment file contains the implementation of the SearchFragment class, which is a UI component
 * that allows users to search for players and games. This fragment is displayed as a tab in the main activity,
 * and it provides a simple search interface that allows users to enter a search query and view the results.
 */
package com.goblin.qrhunter.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.QRCode;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.databinding.FragmentSearchBinding;
import com.goblin.qrhunter.databinding.FragmentSummaryBinding;
import com.goblin.qrhunter.ui.summary.QRcodesArrayAdapter;

import java.util.ArrayList;

/**
 * The SearchFragment class is a UI component that allows users to search for players and games.
 * This fragment is displayed as a tab in the main activity, and it provides a simple search interface
 * that allows users to enter a search query and view the results.
 */
public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;

    // Attributes
    public ArrayList<Player> dataList;
    public ListView playerList;
    public playerSearchArrayAdapter playerAdapter;

    // Search bar
    private MenuItem menuItem;
    private SearchView searchView;

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to. The
     *                           fragment should not add the view itself, but this can be used to generate the
     *                           LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as
     *                           given here.
     * @return Return the View for the fragment's UI, or null.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        // Fragment Set Up
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView textView = binding.textDashboard; // Changed from "binding.textNotifications"
        searchViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);



        // Set up list view, data list (holds the players), and adapter.
        dataList = new ArrayList<>();
        playerList = binding.getRoot().findViewById(R.id.qrcode_list);
        playerAdapter = new playerSearchArrayAdapter(getContext(), dataList);

        // Test: Add a player and display their username
        Player testPlayer = new Player("123456", "Goblins101", "780-123-456");
        dataList.add(testPlayer);
        playerList.setAdapter(playerAdapter);
        return root;
    }

    /**
     * Called when the view previously created by onCreateView() has been detached from the fragment.
     * The next time the fragment needs to be displayed, a new view will be created. This is called after
     * onDestroy().
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}