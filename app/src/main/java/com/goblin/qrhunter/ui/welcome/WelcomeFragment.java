package com.goblin.qrhunter.ui.welcome;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.goblin.qrhunter.R;
import com.goblin.qrhunter.databinding.FragmentWelcomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomeFragment extends Fragment {

    FragmentWelcomeBinding binding;
    FirebaseAuth mAuth;

    public static WelcomeFragment newInstance() {
        return new WelcomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        NavController navController = Navigation.findNavController(container);

        binding.getStartedButton.setOnClickListener(v-> {
            mAuth.signInAnonymously()
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                navController.navigate(R.id.action_welcomeFragment_to_navigation_home);
                                return;
                            }
                            Toast.makeText(getActivity(), "Network error try reconnect", Toast.LENGTH_SHORT);
                            Log.d("AUTH", "Auth Failed: "+ task.getException().getLocalizedMessage());
                        }
                    });
        });
        return binding.getRoot();
    }

}