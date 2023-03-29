package com.goblin.qrhunter.ui.scan;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.QRCode;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.data.PostRepository;
import com.goblin.qrhunter.databinding.FragmentScanBinding;
import com.goblin.qrhunter.databinding.FragmentTakephotoBinding;
//import com.google.android.gms.vision.Frame;
//import com.google.android.gms.vision.barcode.Barcode;
//import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

public class ScanFragment extends Fragment {

    private String TAG = "ScanFragment";
    private ScanViewModel vModel;
    ActivityResultLauncher<Intent> scanActivityIntent;
    Intent scanIntent;

    private FragmentScanBinding binding;

    public static ScanFragment newInstance() {
        return new ScanFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        vModel = new ViewModelProvider(this).get(ScanViewModel.class);

        binding = FragmentScanBinding.inflate(inflater, container, false);
        FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();

        scanActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Log.d(TAG, "onCreateView: activity finished");
                    // Add same code that you want to add in onActivityResult method
                    if(result != null && result.getResultCode() == Activity.RESULT_OK) {
                        Log.d(TAG, "onCreateView: result okay");
                        if(result.getData() != null) {
                            Log.d(TAG, "onCreateView: data not null");
                            String qrString = result.getData().getStringExtra(ScanActivity.QR_CODE_STRING);
                            vModel.setQRCode(qrString);
                            Toast.makeText(this.getContext(), qrString, Toast.LENGTH_SHORT );
                        }
                    }
                    if(result.getResultCode() != Activity.RESULT_OK ) {
                        Log.d(TAG, "onCreateView: activity failed");
                    }

                });

        scanIntent = new Intent(this.getContext(), ScanActivity.class);

        if(savedInstanceState == null) {
            launchScanner();
        }



        return binding.getRoot();
    }

    public void launchScanner() {
        if (vModel == null) {
            vModel = new ViewModelProvider(this).get(ScanViewModel.class);
        }
        scanActivityIntent.launch(scanIntent);
    }

}




