package com.goblin.qrhunter.ui.map;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.databinding.FragmentMapBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

/**
 * The MapFragment class is responsible for displaying a Google Map with markers indicating nearby posts.
 * The user's location is obtained using the FusedLocationProviderClient.
 * If the user grants location permissions, the map will center around their location.
 */
public class MapFragment extends Fragment /*implements OnMapReadyCallback*/ {

    private String TAG = "MapFragment";
    private FragmentMapBinding binding;

    private ActivityResultLauncher<String[]> locationPermissionLauncher;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap map;

    private NavController navController;

    private MapViewModel vModel;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            //TODO: get users location, this can be used as a fallback
            map = googleMap;

            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted, request for the permission
                locationPermissionLauncher.launch(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION});
            }
            // Permission already granted, access the user's location
            fusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                handlePlot(googleMap, task);
            });

        }
    };

    /**
     * Adds markers to the Google Map object indicating nearby posts.
     * If the user has granted location permissions, the user's location will be used as the center of the map.
     *
     * @param googleMap The Google Map object to add the markers to.
     * @param task he task containing the user's location.
     */
    public void handlePlot(GoogleMap googleMap, Task<Location> task) {
        LatLng loc = new LatLng(53.5461, -113.4937);
        String title = "Edmonton";
        if (task.isSuccessful() && task.getResult() != null) {
            loc = new LatLng(task.getResult().getLatitude(), task.getResult().getLongitude());
            title = "Me";
        }
        googleMap.addMarker(new MarkerOptions().position(loc).title(title));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 12.0f));

        vModel.findNearby(loc.latitude, loc.longitude, 100000000)
                .observe(getViewLifecycleOwner(), posts -> {
                    Log.d(TAG, "onMapReady: observing change in nearby posts ");
                    if (posts != null) {
                        for (Post post : posts) {
                            if (post != null) {
                                Log.d(TAG, "onMapReady: trying to add marker");
                                LatLng mark = new LatLng(post.getLat(), post.getLng());
                                Marker mk1 = googleMap.addMarker(new MarkerOptions().position(mark).title(post.getCode().NameGenerator()));
                                if (mk1 == null) {
                                    Log.d(TAG, "onMapReady: failed to add marker, returned null");

                                } else {
                                    Log.d(TAG, "onMapReady: non null marker  name:" + mk1.getTitle());

                                }
                            }

                        }
                    }
                });
    }

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return the view for the fragment's UI, or null.
     */
    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vModel = new ViewModelProvider(this).get(MapViewModel.class);
        binding = FragmentMapBinding.inflate(inflater, container, false);

        assert container != null;
        navController = Navigation.findNavController(container);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());


        locationPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
            if (permissions.get(android.Manifest.permission.ACCESS_FINE_LOCATION) != null && permissions.get(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Permission granted, access the user's location
                fusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                    handlePlot(map, task);
                });
            } else {
                // Permission denied, show a message or disable the location feature
                Toast.makeText(getContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
                navController.popBackStack();
            }
        });

        return binding.getRoot();
    }

    /**
     * Called immediately after onCreateView has returned. This method can be used to access views
     * in the hierarchy.
     *
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

    }

}