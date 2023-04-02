package com.goblin.qrhunter.ui.takephoto;

//import android.Manifest;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.goblin.qrhunter.R;
import com.goblin.qrhunter.ui.addQRCode.addQRCodeFragment;

public class TakePhotoActivity extends AppCompatActivity {

    // Define the pic id
    private static final int pic_id = 123;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    // Define the button and imageview type variable
    Button camera_open_id;
    ImageView click_image_id;
    Button confirm_id;
    Button retake_id;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_takephoto);
        Intent intent = getIntent();
        String qrCode_hash = intent.getStringExtra("qrCode_hash");


        // By ID we can get each component which id is assigned in XML file get Buttons and imageview.
        camera_open_id = findViewById(R.id.camera_button);
        click_image_id = findViewById(R.id.click_image);
        confirm_id=findViewById(R.id.confirm_button);
        retake_id=findViewById(R.id.retake_button);



        // Camera_open button is for open the camera and add the setOnClickListener in this button
        camera_open_id.setOnClickListener(v -> {
            checkCameraPermission();
            camera_open_id.setVisibility(View.INVISIBLE);
            // Set the retake_button visibility to VISIBLE
            retake_id.setVisibility(View.VISIBLE);
        });
        confirm_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConfirmClick(view);
            }

            private void onConfirmClick(View view) {
                if (click_image_id.getDrawable() == null) {
                    Toast.makeText(getApplicationContext(), "Take a picture first", Toast.LENGTH_SHORT).show();
                }

                else {
                    // Get the image from the ImageView
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) click_image_id.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    // TODO: Do something with the bitmap, such as saving it to a file or uploading it to a server
                    Bundle bundle = new Bundle();
                    bundle.putString("qrCode_hash", qrCode_hash);
                    addQRCodeFragment addQRCodeFragment1 = new addQRCodeFragment();
                    addQRCodeFragment1.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.takePhoto_container, addQRCodeFragment1);
                    fragmentTransaction.commit();


                }
            }
        });
        retake_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the click_image_id visibility to GONE
//                click_image_id.setVisibility(View.GONE);
                // Set the retake_button visibility to GONE
                //retake_id.setVisibility(View.GONE);
                // Open the camera for capture the image again
                openCamera();
            }
        });

    }
    // Check if camera permission is granted, if not, request permission
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            openCamera();
        }
    }
    // Open the camera for capture the image
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, pic_id);
    }
    // Handle the permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // This method will help to retrieve the image
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Match the request 'pic id with requestCode
        if (requestCode == pic_id) {
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                click_image_id.setImageBitmap(photo);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
            }
        }
    }



}