package com.goblin.qrhunter.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.goblin.qrhunter.data.PlayerRepository;
import com.goblin.qrhunter.databinding.FragmentOtherProfileBinding;

public class OtherProfileFragment extends Fragment {
    String TAG = "OtherProfileFragment";

    private OtherProfileViewModel mViewModel;
    private FragmentOtherProfileBinding binding;

    public static OtherProfileFragment newInstance() {
        return new OtherProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOtherProfileBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(OtherProfileViewModel.class);
        

        String username = getArguments().getString("username");
        String email = getArguments().getString("contactInfo");


        binding.titleUsername.setText(binding.titleUsername.getText() + username);
        binding.titleEmail.setText(binding.titleEmail.getText() + email);
        return binding.getRoot();
    }

}