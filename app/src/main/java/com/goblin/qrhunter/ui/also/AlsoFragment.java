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

public class AlsoFragment extends Fragment {

    private String TAG="AlsoFragment";
    private AlsoViewModel vModel;
    private FragmentAlsoBinding binding;

    static public String ALSO_FRAGMENT_QR_ARG = "ALSO_FRAGMENT_QR_KEY";


    public static AlsoFragment newInstance() {
        return new AlsoFragment();
    }

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