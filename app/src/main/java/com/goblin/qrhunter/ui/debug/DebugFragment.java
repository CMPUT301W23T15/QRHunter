package com.goblin.qrhunter.ui.debug;

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
import android.widget.Toast;

import com.goblin.qrhunter.R;
import com.goblin.qrhunter.databinding.FragmentDebugBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.goblin.qrhunter.R;

/**
 * A Fragment for the Debug screen, which provides UI to generate random and fixed posts.
 */
public class DebugFragment extends Fragment {

    private DebugViewModel mViewModel;
    private FragmentDebugBinding binding;


    /**
     * Creates a new instance of DebugFragment.
     *
     * @return a new instance of DebugFragment
     */
    public static DebugFragment newInstance() {
        return new DebugFragment();
    }

    /**
     * Sets up the DebugFragment view, initializes the ViewModel, and handles button clicks.
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

        mViewModel = new ViewModelProvider(this).get(DebugViewModel.class);
        NavController navController = Navigation.findNavController(container);

        binding = FragmentDebugBinding.inflate(inflater, container, false);

        binding.debugDoneBtn.setOnClickListener(v -> {
            navController.navigate(R.id.action_debugFragment_to_profileFragment);
        });

        binding.randomPostBtn.setOnClickListener(v -> {
            Task<Void> addTask = mViewModel.generatePost();
            addTask.addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Toast.makeText(getContext(), "Added post", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "network connection error", Toast.LENGTH_SHORT).show();

                }
             });
        });

        binding.constPostBtn.setOnClickListener(v -> {
            Task<Void> addTask = mViewModel.generateFixedPost();
            addTask.addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Toast.makeText(getContext(), "Added post", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "network connection error", Toast.LENGTH_SHORT).show();

                }
            });
        });
        return binding.getRoot();

    }

}