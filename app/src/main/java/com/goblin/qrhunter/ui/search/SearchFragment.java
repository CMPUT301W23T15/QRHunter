/**
 * The SearchFragment file contains the implementation of the SearchFragment class, which is a UI component
 * that allows users to search for players and games. This fragment is displayed as a tab in the main activity,
 * and it provides a simple search interface that allows users to enter a search query and view the results.
 */
package com.goblin.qrhunter.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.goblin.qrhunter.databinding.FragmentSearchBinding;

/**
 * The SearchFragment class is a UI component that allows users to search for players and games.
 * This fragment is displayed as a tab in the main activity, and it provides a simple search interface
 * that allows users to enter a search query and view the results.
 */
public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

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

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        searchViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
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