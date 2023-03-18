package com.goblin.qrhunter.ui.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.data.PlayerRepository;
import com.google.android.gms.tasks.OnFailureListener;


/**
 * A dialog fragment that allows the user to edit their profile information, including phone number and email.
 */
public class EditProfileDialogFragment extends DialogFragment {

    private String TAG = "EditProfileDialogFragment";

    private EditText mPhoneEditText;

    private EditText mEmailEditText;

    private ProfileViewModel vModel;
    private Player mPlayer;

    /**
     * Creates a new instance of the EditProfileDialogFragment.
     *
     * @return The new instance of EditProfileDialogFragment.
     */
    public static EditProfileDialogFragment newInstance(@NonNull Player player) {
        EditProfileDialogFragment fragment = new EditProfileDialogFragment();
        fragment.mPlayer = Player.copy(player);
        return fragment;
    }

    /**
     * Creates and returns a new AlertDialog for the dialog.
     *
     * @param savedInstanceState The saved instance state of the dialog.
     * @return The new AlertDialog for the dialog.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Create the view for the dialog
        View view = getLayoutInflater().inflate(R.layout.fragment_edit_user_profile, null);

        // Find the EditText views
        mPhoneEditText = view.findViewById(R.id.dialog_edit_phone_number);
        mEmailEditText = view.findViewById(R.id.dialog_edit_email);

        mPhoneEditText.setText(mPlayer.getPhone());
        mEmailEditText.setText(mPlayer.getContactInfo());
        if (mPlayer == null) {
            Log.d(TAG, "onCreateDialog: null player passed");
        }


        Button saveButton = view.findViewById(R.id.button_save);
        saveButton.setOnClickListener(v -> {
            String phone = mPhoneEditText.getText().toString();
            String email = mEmailEditText.getText().toString();
            if(isValid(email, phone)) {
                mPlayer.setPhone(phone);
                mPlayer.setContactInfo(email);
                Log.d(TAG, "onCreateDialog: phone set to " + phone + " email set to " + email);
                PlayerRepository playerDB = new PlayerRepository();
                playerDB.update(mPlayer).addOnFailureListener(e -> {
                    Log.e(TAG, "onFailure: ", e);
                    Toast.makeText(getContext(), "Failed to update your profile", Toast.LENGTH_SHORT).show();
                });
                dismiss();
            }

        });
        // Create the dialog and return it
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

    /**
     * Validates the email and phone number input fields.
     *
     * @param email The email input field value.
     * @param phoneNumber The phone number input field value.
     * @return A boolean value indicating whether the input fields are valid or not.
     */
    private boolean isValid(String email, String phoneNumber) {

        // Validate the input fields i.e. check email and phone fields
        if (email == null || phoneNumber == null) {
            return false;
        }

        if (email.isEmpty()) {
            mEmailEditText.setError("Email is required");
            return false;
        }
        if (phoneNumber.isEmpty()) {
            mPhoneEditText.setError("Phone number is required");
            return false;
        }
        if (phoneNumber.length() < 7 || phoneNumber.length() > 15) {
            mPhoneEditText.setError("Phone number must be between 7 and 15 characters");
            return false;
        }
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            mEmailEditText.setError("Invalid email format");
            return false;
        }
        return true;
    }

}
