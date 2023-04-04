package com.goblin.qrhunter.ui.addQRCode;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.goblin.qrhunter.MainActivity;
import com.goblin.qrhunter.R;

import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.QRCode;
import com.goblin.qrhunter.data.PostRepository;
import com.goblin.qrhunter.databinding.FragmentAddQrCodeBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Fragment for the addQRCode screen, handles geotagging and adding the QR code
 */
public class addQRCodeFragment extends Fragment {
    private FragmentAddQrCodeBinding binding;
    private addQRCodeViewModel viewModel;
    String TAG = "addQRCodeFragment";


    private ActivityResultLauncher<String[]> locationPermissionLauncher;
    private static final int REQUEST_LOCATION_PERMISSION = 1;


    String qrCode_hash;


    /**
     * Takes in the qr code that was scanned in by the scanner, and sends it to be hashed / generate a post from it.
     * @param qrCode_hash
     * @return Returns the fragment view.
     */

    public static addQRCodeFragment newInstance(String qrCode_hash) {
        addQRCodeFragment fragment = new addQRCodeFragment();
        Bundle args = new Bundle();
        args.putString("qrCode_hash", qrCode_hash);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This fragment allows the user to add the QR code they scanned into the database -> Reflects in their summary page too.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout for fragment.
        binding = FragmentAddQrCodeBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(addQRCodeViewModel.class);
        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_add_qr_code, container, false);
        // Get the NavController
//        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.navigation_add_QRCode);
//        NavController navController = navHostFragment.getNavController();
        if (getArguments() != null) {
            qrCode_hash = getArguments().getString("qrCode_hash");
        }
        View root = binding.getRoot();

        // Make a new QR code
        QRCode qrCode = new QRCode(qrCode_hash);
        String qrName = qrCode.NameGenerator();
        String hash = qrCode.getHash();
        Log.d(TAG, "add Qr code: " + hash);
        ImageView avatarImageView = binding.qrRepresentaion;

        // Immediately make a new post object (once the view inflates)
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        Post post = new Post();

        // Add the qrCode into the post
        post.setCode(qrCode);
        post.setPlayerId(userId);
        post.setName(qrName);

        // By default, can't see "remove location" button
        binding.buttonRemoveLocation.setVisibility(View.INVISIBLE);

        Glide.with(this)
                .load("https://api.dicebear.com/6.x/bottts-neutral/png?seed=" + hash)
                .placeholder(R.drawable.baseline_qr_code_50)
                .into(avatarImageView);


        // 1. Bind the qr information with layout
        TextView textView_QR_name = binding.textViewQRname;
        ImageView imageView_QR_representation = binding.qrRepresentaion;
        textView_QR_name.setText(qrName);

        // Bind the visual representation here...
        //imageView_QR_representation.setImageBitmap(qrCode.);

        // Cancel button
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(getContext(), "QR code not added.", Toast.LENGTH_SHORT).show();

            }
        });

//        Tag location button
        binding.buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Permission");
                builder.setMessage("Let app use your location?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Add the code to handle the "Yes" button click here
                        Log.d(TAG, "made it here: yes click");

                        // Check if the location permission is granted
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                            // Get the user's current location
                            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
                            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        double latitude = location.getLatitude();
                                        double longitude = location.getLongitude();
                                        Log.d(TAG, "got long lat: line 183");
                                        Log.d(TAG, "lat:"+ latitude);
                                        Log.d(TAG, "long:"+ longitude);
                                        // Sets the post's latitude & longitude (this is the MAIN way).
                                        post.setLat(latitude);
                                        post.setLng(longitude);
                                        Log.d(TAG, "post's lat: " + post.getLat() + ", post's long: " + post.getLng());
                                        String latitudeView = String.valueOf(latitude);
                                        String longitudeView = String.valueOf(longitude);
                                        binding.textViewQRlocation.setText(latitudeView+","+longitudeView);

                                        // If the coordinates get added. Show the button to remove them.
                                        binding.buttonRemoveLocation.setVisibility(View.VISIBLE);
                                        binding.buttonRemoveLocation.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // Show "are u sure" dialog
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                builder.setTitle("Remove Tagged Location");
                                                builder.setMessage("Are you sure?");
                                                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                    // navigate back to homepage
                                                    public void onClick(DialogInterface dialog, int id) {
// Resets coordinate
                                                        post.setLng(0.0);
                                                        post.setLat(0.0);
                                                        binding.textViewQRlocation.setText("No Location Tagged");
                                                        binding.buttonRemoveLocation.setVisibility(View.INVISIBLE);
                                                    }
                                                });
                                                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        ; // Doesn't need to do anything.
                                                    }
                                                });
                                                AlertDialog alert = builder.create();
                                                alert.show();

                                            }
                                        });

                                        // Do something with the user's current location here
                                    } else {
                                        Log.d(TAG, "made it to no location");
                                        // If the location is null, request the user's current location again
                                        LocationRequest locationRequest = LocationRequest.create();
                                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                        locationRequest.setInterval(10000); // Update location every 10 seconds

                                        LocationCallback locationCallback = new LocationCallback() {
                                            @Override
                                            public void onLocationResult(LocationResult locationResult) {
                                                super.onLocationResult(locationResult);
                                                if (locationResult != null) {
                                                    Log.d(TAG, "location result isnt null");
                                                    Location location = locationResult.getLastLocation();
                                                    double latitude = location.getLatitude();
                                                    double longitude = location.getLongitude();
                                                    Log.d(TAG, "got long lat: line 183");
                                                    Log.d(TAG, "lat:"+ latitude);
                                                    Log.d(TAG, "long:"+ longitude);

                                                    // Sets the post's latitude & longitude (this is the secondary way).
                                                    post.setLat(latitude);
                                                    post.setLng(longitude);
                                                    Log.d(TAG, "post's lat: " + post.getLat() + ", post's long: " + post.getLng());
                                                    String latitudeView = String.valueOf(latitude);
                                                    String longitudeView = String.valueOf(longitude);
                                                    binding.textViewQRlocation.setText(latitudeView+","+longitudeView);

                                                    // Do something with the user's current location here
                                                } else {
                                                    // Handle the case where the location is still null
                                                    Log.d(TAG, "still null ");
                                                }
                                            }
                                        };
//                                        no clue if we need this line, not having this line might cause issues tho
//                                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

//                                        not sure
                                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                                    }
                                }
                            });
                        } else {
                            // Permission is not granted, request for the permission
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                            Log.d(TAG, "Permission to get location can not be accessed for w/e reason. ");
                        }



                    }

                });


                // Add the "No" button
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Add the code to handle the "No" button click here
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // Get the current user (what account to add QR code to).
        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    // Add the post into the firebase
                    PostRepository postRepo = new PostRepository();
                    postRepo.add(post).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Post added successfully");

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("QR code successfully added");
                            builder.setMessage("Name: " + qrName + '\n' + "Score: " + post.getCode().getScore());
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                // navigate back to homepage
                                public void onClick(DialogInterface dialog, int id) {

                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                }
                            });

                            AlertDialog alert = builder.create();
                            alert.show();

                        } else {
                            Log.e(TAG, "Error adding post", task.getException());
                        }

                    });

                }
            }
        });


        // Create a new instance of HomeViewModel
        // viewModel = new ViewModelProvider(this).get(confirmAddQRCodeViewModel.class);
        return binding.getRoot();
    }
}
