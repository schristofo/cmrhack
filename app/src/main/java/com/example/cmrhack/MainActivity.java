package com.example.cmrhack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.exifinterface.media.ExifInterface;
import android.graphics.Matrix;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    int SELECT_PICTURE = 200;
    Button button;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize main Application view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the default image preview
        imageView = (ImageView) findViewById(R.id.imageView);

        // Initialize button
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent: used to perform an action on another app - use ACTION_GET_CONTENT and image/* to get images
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");

                // Chooser acts as a wrapper for intent: used to choose a photo from the preferred photo viewer
                Intent chooser = Intent.createChooser(photoPickerIntent, "Choose a Picture");
                // Trigger Chooser intent
                startActivityForResult(chooser, SELECT_PICTURE);
            }
        });
    }

    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        // Verify that the function was called with the correct arguments
        if (reqCode == SELECT_PICTURE && resultCode == RESULT_OK) {

            try {
                // Get image URI
                Uri imageUri = data.getData();
                int orient;
                long dat;

                // Open image as an InputStream
                InputStream imageStream = this.getContentResolver().openInputStream(imageUri);

                // Convert image to bitmap with ARGB 32bit format
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                options.inMutable = true;
                Bitmap myBitmap = BitmapFactory.decodeStream(imageStream, null, options);

                // Close the InputStream
                imageStream.close();

                // Re-open image as an InputStream
                imageStream = this.getContentResolver().openInputStream(imageUri);

                // Get image orientation and date/time parameters
                ExifInterface exifInterface = new ExifInterface(imageStream);
                orient = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                dat = exifInterface.getDateTime();
                Date currentDate = new Date(dat);
                DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");

                // Close the InputStream
                imageStream.close();

                // Use Matrix class to rotate the image depending on the orientation
                Matrix matrix = new Matrix();
                switch (orient) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        matrix.postRotate(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix.postRotate(180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        matrix.postRotate(270);
                        break;
                }
                Bitmap bm = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true);

                // Use for testing only
//                for(int y=0;y<bm.getHeight();y++){
//                    bm.setPixel(100,y,Color.argb(254,254,254,254));
//                    bm.setPixel(101,y,Color.argb(254,254,254,254));
//                    bm.setPixel(102,y,Color.argb(254,254,254,254));
//                }

                // Preview image in the image view
                imageView.setImageBitmap(bm);

                // Generate new image title based on the date captured
                String name = "IMG_" + df.format(currentDate);
                // Save image
                MediaStore.Images.Media.insertImage(this.getContentResolver(), bm, name, "A description");
            } catch (Exception E) {
                Toast.makeText(this, "Couldn't load image", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "Action aborted", Toast.LENGTH_LONG).show();
        }
    }
}