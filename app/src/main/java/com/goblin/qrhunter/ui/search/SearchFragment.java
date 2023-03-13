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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.databinding.FragmentSearchBinding;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * The SearchFragment class is a UI component that allows users to search for players and games.
 * This fragment is displayed as a tab in the main activity, and it provides a simple search interface
 * that allows users to enter a search query and view the results.
 */
public class SearchFragment extends Fragment implements MenuProvider{
    private FragmentSearchBinding binding;

    // Attributes
    public ArrayList<Player> dataList;
    public ListView playerList;
    public playerSearchAdapter searchAdapter;

    // Search bar
    private MenuItem menuItem;
    private SearchView searchView;

    // Results from search
    private RecyclerView search_results_view;


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

        // Set up search bar
        MenuHost menuHost = getActivity();
        assert menuHost != null;
        menuHost.addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        // Set up recycler view
        search_results_view = binding.getRoot().findViewById(R.id.search_players_results);
        search_results_view.setLayoutManager(new LinearLayoutManager(getContext()));

        // Access to firebase
        FirebaseRecyclerOptions<Player> options =
                new FirebaseRecyclerOptions.Builder<Player>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("players"), Player.class)
                        .build();

        searchAdapter = new playerSearchAdapter(options);
        search_results_view.setAdapter(searchAdapter);
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

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        // Inflate the menu stuff
        menuInflater.inflate(R.menu.nav_search_menu, menu);
        MenuItem item = menu.findItem(R.id.player_search);
        searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                process_search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                process_search(newText);
                return false;
            }
        });
    }



    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        Toast.makeText(getContext(), "search bar clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    // Method to search fire base
    private void process_search(String s){
        FirebaseRecyclerOptions<Player> options =
                new FirebaseRecyclerOptions.Builder<Player>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("players")
                                .orderByChild("username")
                                .startAt(s).endAt(s+"\uf8ff"), Player.class)
                        .build();

        searchAdapter = new playerSearchAdapter(options);
        searchAdapter.startListening();
        search_results_view.setAdapter(searchAdapter);
    }
    @Override public void onStart()
    {
        super.onStart();
        searchAdapter.startListening();
        Toast.makeText(getContext(), "listening", Toast.LENGTH_SHORT).show();
    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override public void onStop()
    {
        super.onStop();
        searchAdapter.stopListening();
    }
}