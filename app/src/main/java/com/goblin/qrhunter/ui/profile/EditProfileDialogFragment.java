package com.goblin.qrhunter.ui.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.goblin.qrhunter.R;


public class EditProfileDialogFragment extends DialogFragment {

    private EditText mPhoneEditText;
    private EditText mEmailEditText;
    private ProfileViewModel mViewModel;

    public static EditProfileDialogFragment newInstance(ProfileViewModel viewModel) {
        EditProfileDialogFragment fragment = new EditProfileDialogFragment();
        fragment.mViewModel = viewModel;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create the view for the dialog
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_user_profile, null);

        // Find the EditText views
        mPhoneEditText = view.findViewById(R.id.edit_phone_number);
        mEmailEditText = view.findViewById(R.id.edit_email);

        // Find the "Save" button and set an OnClickListener
        Button saveButton = view.findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the new values entered by the user
                String newPhoneNumber = mPhoneEditText.getText().toString();
                String newEmail = mEmailEditText.getText().toString();

                // Validate the input fields
                if (newEmail.isEmpty()) {
                    mEmailEditText.setError("Email is required");
                    return;
                }
                if (newPhoneNumber.isEmpty()) {
                    mPhoneEditText.setError("Phone number is required");
                    return;
                }
                if (newPhoneNumber.length() < 7 || newPhoneNumber.length() > 15) {
                    mPhoneEditText.setError("Phone number must be between 7 and 15 characters");
                    return;
                }
                if (!newEmail.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    mEmailEditText.setError("Invalid email format");
                    return;
                }

                // Update the LiveData objects in the ProfileViewModel
                if (mViewModel != null) {
                    mViewModel.setPhoneNumber(newPhoneNumber);
                    mViewModel.setEmail(newEmail);
                }

                // Close the dialog
                dismiss();
            }
        });

        // Create the dialog and return it
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Observe the phone number LiveData in the ProfileViewModel if mViewModel is not null
        if (mViewModel != null) {
            mViewModel.getPhoneNumber().observe(this, new Observer<String>() {
                @Override
                public void onChanged(String phoneNumber) {
                    mPhoneEditText.setText(phoneNumber);
                }
            });

            // Observe the email LiveData in the ProfileViewModel if mViewModel is not null
            mViewModel.getEmail().observe(this, new Observer<String>() {
                @Override
                public void onChanged(String email) {
                    mEmailEditText.setText(email);
                }
            });
        }
    }
}
