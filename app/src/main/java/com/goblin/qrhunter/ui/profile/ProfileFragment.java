/**
 * This package contains classes related to the user profile screen.
 * The ProfileFragment class is the main fragment for displaying the user's profile information,
 * including their username and a button to sign out.
 * The ProfileViewModel class provides the data and logic for the ProfileFragment.
 */
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

/**
 * A simple {@link Fragment} subclass.
 * Displays the profile of the current player.
 */
public class ProfileFragment extends Fragment {

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