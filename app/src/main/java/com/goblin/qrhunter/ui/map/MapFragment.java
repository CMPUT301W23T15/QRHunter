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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;

public class MapFragment extends Fragment /*implements OnMapReadyCallback*/ {
    // Attributes
    private MapViewModel mViewModel;
    GoogleMap gMap;
    FrameLayout map;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //SupportMapFragment mapFragment = (SupportMapFragment) getParentFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    /*
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;
        LatLng myPos = new LatLng(53.5461, 113.4937);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myPos));
    }

     */

}