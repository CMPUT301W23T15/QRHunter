package com.goblin.qrhunter.ui.scan;

import static java.security.AccessController.getContext;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.goblin.qrhunter.MainActivity;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.ui.home.HomeFragment;
import com.goblin.qrhunter.ui.takephoto.TakePhotoActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

//import com.google.android.gms.vision.Frame;
//import com.google.android.gms.vision.barcode.Barcode;
//import com.google.android.gms.vision.barcode.BarcodeDetector;

public class ScanActivity extends AppCompatActivity {

    private static final String TAG = ScanActivity.class.getSimpleName();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;
    public static String QR_CODE_STRING = "QR_CODE_STRING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //need details what function is related with which xml
        setContentView(R.layout.fragment_scan);

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode or QR Code, click 'esc' to quit");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setCaptureActivity(CaptureAct.class);
        intentIntegrator.initiateScan();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
            builder.setTitle("Result");
            builder.setMessage(intentResult.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                    // create an alert dialog with two buttons
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(ScanActivity.this);
                    builder2.setMessage("Do you want to take a photo?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // open the TakePhotoActivity
                                    Intent intent = new Intent(ScanActivity.this, TakePhotoActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // navigate to another activity
                                    Intent intent = new Intent(ScanActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                    builder2.show();
                }
            });
            builder.show();
        } else {
            Toast.makeText(getApplicationContext(),
                            "OOPs...no QR code was scanned", Toast.LENGTH_SHORT)
                    .show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("fragmentToLoad", "homeFragment"); // pass the tag or identifier of your HomeFragment here
            startActivity(intent);
            finish();
        }
    }
}






