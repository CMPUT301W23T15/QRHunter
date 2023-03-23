/**
 * This package contains classes related to the user profile screen.
 * The ProfileFragment class is the main fragment for displaying the user's profile information,
 * including their username and a button to sign out.
 * The ProfileViewModel class provides the data and logic for the ProfileFragment.
 */
package com.goblin.qrhunter.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.databinding.FragmentUserProfileBinding;
import com.goblin.qrhunter.ui.welcome.WelcomeActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Displays the profile of the current player.
 */
public class ProfileFragment extends Fragment {

    private String TAG="ProfileFragment";
    private ProfileViewModel mViewModel;

    private FragmentUserProfileBinding binding;

    /**
     * Returns a new instance of the fragment.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        NavController navController = Navigation.findNavController(container);
        TextView txtView = binding.titleUsername;

        mViewModel.getLivePlayer().observe(getViewLifecycleOwner(), player -> {
            Log.d(TAG, "onCreateView: ");
            if (player != null) {
                binding.titleUsername.setText("Username: " + player.getUsername());
                binding.titleEmail.setText("Email: " + player.getContactInfo());
                binding.titlePhone.setText("Phone: " + player.getPhone());
                binding.buttonEditProfile.setOnClickListener(v -> {
                    EditProfileDialogFragment dialog = EditProfileDialogFragment.newInstance(player);
                    dialog.show(getChildFragmentManager(), "EditProfileDialogFragment");
                });
            }
        });



        binding.buttonTestSignout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent welcomeIntent = new Intent(getContext(), WelcomeActivity.class);
            startActivity(welcomeIntent);
        });


        binding.debugButton.setOnClickListener(v -> {
            navController.navigate(R.id.action_profileFragment_to_debugFragment);
        });
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

    }


}