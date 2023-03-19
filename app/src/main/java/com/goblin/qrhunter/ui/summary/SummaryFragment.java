/**
 * The summary view is implemented using the Fragment class in Android, which provides a modular way to
 * display a section of the user interface. The view is created by inflating a layout file using the
 * LayoutInflater and binding the views to data using the FragmentSummaryBinding class.
 */
package com.goblin.qrhunter.ui.summary;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.databinding.FragmentSummaryBinding;
import com.goblin.qrhunter.ui.listutil.QRRecyclerAdapter;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * The SummaryFragment class is responsible for displaying a summary of information in the application.
 * It uses the SummaryViewModel class to manage the data and logic behind the summary view.
 */
public class SummaryFragment extends Fragment {
    private SummaryViewModel mViewModel;
    private FragmentSummaryBinding binding;
    FirebaseFirestore db;
    String TAG = "SummaryFragment";


    /**
     Called to have the fragment instantiate its user interface view.
     @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     @return The fragment's view.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(SummaryViewModel.class);
        binding = FragmentSummaryBinding.inflate(inflater, container, false);

        // set username.
        mViewModel.getUsername().observe(getViewLifecycleOwner(), binding.textDashboard::setText);

        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.qrListView.setLayoutManager(llm);
        // create a qr list
        QRRecyclerAdapter qrAdapter = new QRRecyclerAdapter();
        binding.qrListView.setAdapter(qrAdapter);

        // listen for changes and update list
        mViewModel.getUserPosts().observe(getViewLifecycleOwner(), qrAdapter::setData);
        mViewModel.getUserPosts().observe(getViewLifecycleOwner(), posts -> {
            for (Post p: posts
                 ) {
                Log.d(TAG, "onCreateView: updating: " + p.getId() );
            }

        });


        return binding.getRoot();
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