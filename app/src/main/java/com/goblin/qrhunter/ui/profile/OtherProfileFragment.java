/**
 * Displays another players profile
 */
package com.goblin.qrhunter.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.databinding.FragmentOtherProfileBinding;
import com.goblin.qrhunter.ui.listutil.QRRecyclerAdapter;

/**
 * The OtherProfileFragment class is a Fragment that displays the profile of a selected user.
 * It contains a ViewModel object to manage the data for the fragment and a binding object to interact with the UI elements.
 * The class has static String constants to hold keys for arguments passed to the fragment.
 */
public class OtherProfileFragment extends Fragment {
    String TAG = "OtherProfileFragment";

    private OtherProfileViewModel mViewModel;
    private FragmentOtherProfileBinding binding;

    static public String PLAYER_ID_ARG_KEY = "playerId";

    static public String PLAYER_ARG_KEY = "PLAYER_ARG";

    static public String PLAYER_USERNAME_ARG_KEY = "username";
    static public String PLAYER_PHONE_ARG_KEY = "phone";
    static public String PLAYER_CONTACT_INFO_ARG_KEY = "contactInfo";

    private Player selectedPlayer;

    public static OtherProfileFragment newInstance() {
        return new OtherProfileFragment();
    }

    /**
     * Called to have the fragment instantiate its view.
     * It inflates the layout for this fragment, retrieves the arguments passed to the fragment,
     * and sets up the UI to display
     * the user's profile information and a list of posts made by the user.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached
     *                           to. The fragment should not add the view itself,
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI, or null.
     * @see Fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOtherProfileBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(OtherProfileViewModel.class);

        assert getArguments() != null;
        Player player = getArguments().getSerializable(PLAYER_ARG_KEY, Player.class);
        assert  player != null;

        String username = player.getUsername();
        String phone = player.getPhone();
        String contactInfo = player.getContactInfo();

        binding.titleUsername.setText("User: " + username);
        binding.titleEmail.setText("Email: " + contactInfo);
        binding.titlePhone.setText("Phone: " + phone);
        // (On-create)
        try {
            mViewModel.getPlayerByUsername(username);
            QRRecyclerAdapter adapter = new QRRecyclerAdapter();
            binding.listScannedQR.setAdapter(adapter);
            mViewModel.getPlayerPosts().observe(getViewLifecycleOwner(), posts -> {
                int score = 0;
                adapter.setData(posts);
                for (Post post : posts) {
                    if (post.getCode() != null) {
                        score += post.getCode().getScore();
                    }

                }
                binding.titleTotalScore.setText("Total Score: " + score);

            });

        } catch (Exception e) {
            Log.e(TAG, "onCreateView: ", e);
            Toast.makeText(getContext(), "Failed to load player", Toast.LENGTH_SHORT).show();
        }

        // 1) Get all of the (searched) users' QR codes.
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.listScannedQR.setLayoutManager(llm);
        // 2) Set up the list of QR codes.
        QRRecyclerAdapter qrAdapter = new QRRecyclerAdapter();
        binding.listScannedQR.setAdapter(qrAdapter);

        // 3) Listen for any changes in the user's QR codes & update accordingly.
        mViewModel.getPlayerPosts().observe(getViewLifecycleOwner(), qrAdapter::setData);
        mViewModel.getPlayerPosts().observe(getViewLifecycleOwner(), posts -> {
            for (Post p: posts
            ) {
                Log.d(TAG, "onCreateView: updating: " + p.getId() );
            }

        });

        return binding.getRoot();
    }

}