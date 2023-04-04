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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.goblin.qrhunter.MainActivity;
import com.goblin.qrhunter.QRCode;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.ui.addQRCode.addQRCodeFragment;
import com.goblin.qrhunter.ui.home.HomeFragment;
import com.goblin.qrhunter.ui.takephoto.TakePhotoActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

//import com.google.android.gms.vision.Frame;
//import com.google.android.gms.vision.barcode.Barcode;
//import com.google.android.gms.vision.barcode.BarcodeDetector;

/**
 * Activity which handles scanning the QR Code
 */
public class ScanActivity extends AppCompatActivity {

    private static final String TAG = ScanActivity.class.getSimpleName();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;
    public static String QR_CODE_STRING = "QR_CODE_STRING";
    FirebaseFirestore db;

    /**
     * OnCreate to set up the QR code scanner.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
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

    /**
     * Run analysis on a QR code once it has been scanned.
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null && intentResult.getContents() != null) {
            // Hash the content into score
            String qrCode_hash=intentResult.getContents();
            QRCode qrCode = new QRCode(qrCode_hash);
            int qrscore = qrCode.getScore();
            AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
            builder.setTitle("Your QR code is worth:");
            builder.setMessage(String.valueOf(qrscore));
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });




            // create an alert dialog with two buttons
            AlertDialog.Builder builder2 = new AlertDialog.Builder(ScanActivity.this);

            builder2.setMessage("Do you want to take a photo?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // open the TakePhotoActivity
                            Intent intent = new Intent(ScanActivity.this, TakePhotoActivity.class);
                            intent.putExtra("qrCode_hash", qrCode_hash);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        //QR hashed value is saved to database

                        public void onClick(DialogInterface dialog, int id) {
                            // Create a bundle to pass data to the new fragment
                            Bundle bundle = new Bundle();
                            bundle.putString("qrCode_hash", qrCode_hash);
                            addQRCodeFragment addQRCodeFragment1 = new addQRCodeFragment();
                            addQRCodeFragment1.setArguments(bundle);
                            // Get the NavController and navigate to the AddQRFragment
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.add(R.id.scan_container, addQRCodeFragment1);
                            fragmentTransaction.commit();
                        }
                    });

            builder2.show();
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






