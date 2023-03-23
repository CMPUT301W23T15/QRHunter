package com.goblin.qrhunter.ui.takephoto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.goblin.qrhunter.R;

public class TakePhotoActivity extends AppCompatActivity {

    // Define the pic id
    private static final int pic_id = 123;
    // Define the button and imageview type variable
    Button camera_open_id;
    ImageView click_image_id;
    Button confirm_id;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_scan);

        // By ID we can get each component which id is assigned in XML file get Buttons and imageview.
        camera_open_id = findViewById(R.id.camera_button);
        click_image_id = findViewById(R.id.click_image);
        confirm_id=findViewById(R.id.confirm_button);



        // Camera_open button is for open the camera and add the setOnClickListener in this button
        camera_open_id.setOnClickListener(v -> {
            // Create the camera_intent ACTION_IMAGE_CAPTURE it will open the camera for capture the image
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Start the activity with camera_intent, and request pic id
            startActivityForResult(camera_intent, pic_id);
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
                    // Do something with the bitmap, such as saving it to a file or uploading it to a server
                }
            }
        });

    }

    // This method will help to retrieve the image
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Match the request 'pic id with requestCode
        if (requestCode == pic_id) {
            // BitMap is data structure of image file which store the image in memory
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // Set the image in imageview for display
            click_image_id.setImageBitmap(photo);
        }
    }
}