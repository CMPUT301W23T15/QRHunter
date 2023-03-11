/**
 * The summary view is implemented using the Fragment class in Android, which provides a modular way to
 * display a section of the user interface. The view is created by inflating a layout file using the
 * LayoutInflater and binding the views to data using the FragmentSummaryBinding class.
 */
package com.goblin.qrhunter.ui.summary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.goblin.qrhunter.databinding.FragmentSummaryBinding;

/**
 * The SummaryFragment class is responsible for displaying a summary of information in the application.
 * It uses the SummaryViewModel class to manage the data and logic behind the summary view.
 */
public class SummaryFragment extends Fragment {

    private FragmentSummaryBinding binding;

    /**

     Called to have the fragment instantiate its user interface view.

     @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.

     @param container If non-null, this is the parent view that the fragment's UI should be attached to.

     @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.

     @return The fragment's view.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // SummaryViewModel summaryViewModel = new ViewModelProvider(this).get(SummaryViewModel.class);

        binding = FragmentSummaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        return root;
    }

    /**

     Called when the view previously created by onCreateView has been detached from the fragment.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}