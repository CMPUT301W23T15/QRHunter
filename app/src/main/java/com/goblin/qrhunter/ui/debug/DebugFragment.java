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

public class DebugFragment extends Fragment {

    private DebugViewModel mViewModel;
    private FragmentDebugBinding binding;


    public static DebugFragment newInstance() {
        return new DebugFragment();
    }

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