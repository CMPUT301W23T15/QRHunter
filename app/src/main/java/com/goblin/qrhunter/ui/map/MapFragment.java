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


public class MapFragment extends Fragment /*implements OnMapReadyCallback*/ {
    // Attributes
    private MapViewModel mViewModel;


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


}