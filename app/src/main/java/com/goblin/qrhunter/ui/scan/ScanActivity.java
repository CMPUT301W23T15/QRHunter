package com.goblin.qrhunter.ui.scan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class ScanActivity extends AppCompatActivity {

    private static final String TAG = ScanActivity.class.getSimpleName();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;
    public static String QR_CODE_STRING  = "QR_CODE_STRING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //need details what function is related with which xml
        //setContentView(R.layout.fragment_scan);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted. Request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            // Launch the camera to scan the QR code
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");

            // Use the Google Vision API to scan the QR code and extract the data from it
            BarcodeDetector detector = new BarcodeDetector.Builder(getApplicationContext())
                    .setBarcodeFormats(Barcode.QR_CODE)
                    .build();

            if (!detector.isOperational()) {
                Log.w(TAG, "Could not set up the detector!");
                finish();
                return;
            }

            Frame frame = new Frame.Builder().setBitmap(photo).build();
            SparseArray<Barcode> barcodes = detector.detect(frame);

            if (barcodes.size() == 0) {
                Log.w(TAG, "No QR code found in the image!");
                finish();
                return;
            }

            Barcode barcode = barcodes.valueAt(0);
            String qrCodeData = barcode.displayValue;

            // Handle the scanned data as needed (e.g., display it to the user, save it to a file or database, etc.)
            Log.d(TAG, "QR code data: " + qrCodeData);
            getIntent().putExtra("QR_CODE_STRING", qrCodeData);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Launch the camera to scan the QR code
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            } else {
                // Permission denied, handle the error or prompt the user again
                Log.w(TAG, "Camera permission denied!");
            }
        }
    }
}
