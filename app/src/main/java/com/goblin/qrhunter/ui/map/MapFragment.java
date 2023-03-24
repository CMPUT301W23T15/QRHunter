package com.goblin.qrhunter.ui.map;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.goblin.qrhunter.R;
import com.google.type.LatLng;


public class MapFragment extends Fragment /*implements OnMapReadyCallback*/ {
    // Attributes
    private MapViewModel mViewModel;
//    private GoogleMap googleMap;
    // GoogleMap gMap;
    // FrameLayout map;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


//
//        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
//
//        mapView = rootView.findViewById(R.id.map_view);
//        mapView.onCreate(savedInstanceState);
//
//        mapView.getMapAsync(this);
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    /*@Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        // Add markers, camera movement, and other map manipulation here
    }
*/

}