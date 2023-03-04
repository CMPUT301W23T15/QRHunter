package com.goblin.qrhunter.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.goblin.qrhunter.R;
import com.goblin.qrhunter.databinding.FragmentHomeBinding;
import com.goblin.qrhunter.databinding.FragmentProfileBinding;
import com.goblin.qrhunter.ui.home.HomeViewModel;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;

    private FragmentProfileBinding binding;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        TextView txtView = binding.usernameTextView;

        mViewModel.getUsername().observe(getViewLifecycleOwner(), txtView::setText);
        return binding.getRoot();
    }


}