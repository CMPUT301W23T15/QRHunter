package com.goblin.qrhunter.ui.scan;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.QRCode;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.data.PostRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ScanFragment extends Fragment {

    private ScanViewModel vModel;

    public static ScanFragment newInstance() {
        return new ScanFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scan, container, false);
        FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();



        root.findViewById(R.id.button_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return root;
    }


}