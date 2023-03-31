package com.goblin.qrhunter.ui.takephoto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.goblin.qrhunter.databinding.FragmentAddQrCodeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class addQRCodeFragment {
    private FragmentAddQrCodeBinding binding;
    private addQRCodeViewModel viewModel;
    String TAG = "addQRCodeFragment";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout for fragment.
        binding = FragmentAddQrCodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Get the current user (what account to add QR code to).
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


        // Create a new instance of HomeViewModel
        // viewModel = new ViewModelProvider(this).get(confirmAddQRCodeViewModel.class);
        return binding.getRoot();
    }
}
