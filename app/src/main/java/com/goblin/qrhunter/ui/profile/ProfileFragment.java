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
import com.goblin.qrhunter.databinding.FragmentUserProfileBinding;
import com.goblin.qrhunter.databinding.FragmentUserProfileBinding;
import com.goblin.qrhunter.ui.home.HomeViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;

    private FragmentUserProfileBinding binding;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        TextView txtView = binding.titleUsername;
        mViewModel.getUsername().observe(getViewLifecycleOwner(), txtView::setText);

        binding.buttonTestSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
            }
        });
        return binding.getRoot();
    }


}