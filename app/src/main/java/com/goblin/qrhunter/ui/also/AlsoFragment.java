package com.goblin.qrhunter.ui.also;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goblin.qrhunter.R;
import com.goblin.qrhunter.databinding.FragmentAlsoBinding;
import com.goblin.qrhunter.ui.home.HomeViewModel;

/**
 * A Fragment for displaying data related to a specific QR code.
 */
public class AlsoFragment extends Fragment {

    private String TAG="AlsoFragment";
    private AlsoViewModel vModel;
    private FragmentAlsoBinding binding;

    /**
     * A constant string for passing the QR code hash as an argument to the AlsoFragment.
     */
    static public String ALSO_FRAGMENT_QR_ARG = "ALSO_FRAGMENT_QR_KEY";

    /**
     * Creates a new instance of AlsoFragment.
     *
     * @return a new instance of AlsoFragment
     */
    public static AlsoFragment newInstance() {
        return new AlsoFragment();
    }

    /**
     * Sets up the AlsoFragment view, initializes the ViewModel, and displays data related
     * to the provided QR code hash.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAlsoBinding.inflate(inflater, container, false);

        if(getArguments() == null) {
            return binding.getRoot();
        }
        String hash = getArguments().getString(ALSO_FRAGMENT_QR_ARG);
        if(hash == null )  {
            return binding.getRoot();
        }

        Log.d(TAG, "onCreateView: hash is " + hash);

        vModel = new ViewModelProvider(this).get(AlsoViewModel.class);

        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.alsoScannedListView.setLayoutManager(llm);

        AlsoRecyclerAdapter adapter = new AlsoRecyclerAdapter();
        binding.alsoScannedListView.setAdapter(adapter);
        vModel.getByQR(hash).observe(getViewLifecycleOwner(), adapter::setData);

        return binding.getRoot();
    }



}