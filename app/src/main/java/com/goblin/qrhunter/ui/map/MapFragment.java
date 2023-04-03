package com.goblin.qrhunter.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.goblin.qrhunter.BuildConfig;
import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.databinding.FragmentMapBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.protobuf.DescriptorProtos;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;


public class MapFragment extends Fragment /*implements OnMapReadyCallback*/ {

    private String TAG = "MapFragment";
    private FragmentMapBinding binding;

    private FusedLocationProviderClient locationClient;

    private GoogleMap map;

    private NavController navController;

    private MapViewModel vModel;

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

    // Set the fields to specify which types of place data to
// return after the user has made a selection.
    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

    // Create an instance of Autocomplete.IntentBuilder with the appropriate parameters.
    Autocomplete.IntentBuilder intentBuilder = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields);

    // Create an ActivityResultLauncher by calling the registerForActivityResult() method.
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // Handle the result data here.
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    handlePlot(map, place.getLatLng(), place.getName());
                }
            }
    );




    @SuppressLint("MissingPermission")
    private final ActivityResultLauncher<String[]> locationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                for(Map.Entry<String, Boolean> entry: result.entrySet()) {
                    if(!entry.getValue()) {
                        navController.popBackStack();
                        return;
                    }
                }
                locationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnCompleteListener(task -> {
                    handleTask(map, task);
                });

            });

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


            if (ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(requireContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            ) {
                locationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnCompleteListener(task -> {
                    handleTask(map, task);
                });
            }  else {
                Log.d(TAG, "onMapReady: permissions not granted");
                locationPermissionLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION});
            }

            binding.searchBtn.setOnClickListener(view -> {
                // Call the launch() method on the ActivityResultLauncher to start the autocomplete intent.
                launcher.launch(intentBuilder.build(requireContext()));

            });
        }
    };



    private void handleTask(@NonNull GoogleMap map, @NonNull Task<Location> task) {
        String name = "Me";
        LatLng loc = new LatLng(53.545883, -113.490112);
        if(task.isSuccessful() && task.getResult() != null) {
            loc = new LatLng(task.getResult().getLatitude(), task.getResult().getLongitude());
            name = "YEG";
        } else {
            Log.d(TAG, "handleTask: non location from task");
        }
        handlePlot(map, loc, name);
    }

    private void handlePlot(GoogleMap googleMap, LatLng latLng, String name) {
        String title = name;
        if (latLng == null) {
            title = "Edmonton";
            latLng = new LatLng(53.545883, -113.490112);
        }

        googleMap.addMarker(new MarkerOptions().position(latLng).title(title));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));

        vModel.findNearby(latLng.latitude, latLng.longitude, 100000000)
                .observe(getViewLifecycleOwner(), posts -> {
                    Log.d(TAG, "onMapReady: observing change in nearby posts ");
                    if (posts != null) {
                        for (Post post : posts) {
                            if (post != null) {
                                LatLng mark = new LatLng(post.getLat(), post.getLng());
                                Marker mk1 = googleMap.addMarker(new MarkerOptions().position(mark).title(post.getName()));

                            }

                        }
                    }
                });
    }


    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vModel = new ViewModelProvider(this).get(MapViewModel.class);
        binding = FragmentMapBinding.inflate(inflater, container, false);

        assert container != null;
        navController= Navigation.findNavController(container);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationClient = LocationServices.getFusedLocationProviderClient(getContext());
        if(!Places.isInitialized()) {

            Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY);
        }
    }


}