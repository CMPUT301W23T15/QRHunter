package com.goblin.qrhunter.ui.map;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.firebase.geofire.GeoLocation;
import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.databinding.FragmentMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class MapFragment extends Fragment /*implements OnMapReadyCallback*/ {

    private String TAG="MapFragment";
    private FragmentMapBinding binding;
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
            LatLng yeg = new LatLng(53.5461, -113.4937);


            googleMap.resetMinMaxZoomPreference();
            googleMap.setMinZoomPreference(12);
            googleMap.setMaxZoomPreference(18);
            googleMap.addMarker(new MarkerOptions().position(yeg).title("Edmonton"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(yeg));

            vModel.findNearby(yeg.latitude, yeg.longitude, 100000000)
                    .observe(getViewLifecycleOwner(), posts -> {
                        Log.d(TAG, "onMapReady: observing change in nearby posts ");
                        if(posts != null ) {
                            for (Post post: posts) {
                                if(post != null) {
                                    Log.d(TAG, "onMapReady: trying to add marker");
                                    LatLng mark = new LatLng(post.getLat(), post.getLng());
                                    Marker mk1 =  googleMap.addMarker(new MarkerOptions().position(mark).title(post.getName()));
                                    if(mk1 == null) {
                                        Log.d(TAG, "onMapReady: failed to add marker, returned null");

                                    } else {
                                        Log.d(TAG, "onMapReady: non null marker  name:" + mk1.getTitle());

                                    }
                                }

                            }
                        }
                    });


        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vModel = new ViewModelProvider(this).get(MapViewModel.class);
        binding = FragmentMapBinding.inflate(inflater, container, false);


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



}