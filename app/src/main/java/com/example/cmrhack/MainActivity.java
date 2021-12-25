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
import java.util.Date;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    int SELECT_PICTURE = 200;
    int imgWidth;
    int imgHeight;
    Button button;
    ImageView imageView;
    Bitmap bm;

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
                imgWidth = myBitmap.getWidth();
                imgHeight = myBitmap.getHeight();

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
                bm = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true);

                setStrip(300, 310, 4, 4);
                setStrip(515, 527, 4, 4);
                setStrip(570, 577, 4, 4);
                setStrip(935, 946, 4, 4);
                setStrip(979, 991, 4, 4);
                setStrip(1020,1030, 4, 4);
                setStrip(1200,1211, 4, 4);
                setStrip(1280,1291, 4, 4);
                setStrip(1391,1403, 4, 4);
                setStrip(1455,1468, 4, 4);
                setStrip(1482,1495, 4, 4);

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

    /**
     Eliminate vertical strip of hot pixels in an image.
     @param x1 horizontal coordinate of the first pixel (left) of the vertical strip range
     @param x2 horizontal coordinate of the last pixel (right) of the vertical strip range
     @param rand random permutation to be applied in every pixel of the strip
     @param neigh number of strip's neighbor pixels that will be taken into account
     @return void
     */
    public void setStrip(int x1, int x2, int rand, int neigh)
    {
        for(int y=0; y<imgWidth; y++){
            int r1 = 0;
            int r2 = 0;
            int g1 = 0;
            int g2 = 0;
            int b1 = 0;
            int b2 = 0;
            int r;
            int g;
            int b;

            for(int i=0; i<neigh; i++){
                r1 += (bm.getPixel(x1-i,y) >> 16) & 0xff;
                r2 += (bm.getPixel(x2+i,y) >> 16) & 0xff;
                g1 += (bm.getPixel(x1-i,y) >> 8) & 0xff;
                g2 += (bm.getPixel(x2+i,y) >> 8) & 0xff;
                b1 += bm.getPixel(x1-i,y) & 0xff;
                b2 += bm.getPixel(x2+i,y) & 0xff;
            }
            float fr1 = (float)r1/neigh;
            float fr2 = (float)r2/neigh;
            float fg1 = (float)g1/neigh;
            float fg2 = (float)g2/neigh;
            float fb1 = (float)b1/neigh;
            float fb2 = (float)b2/neigh;

            for(int x=x1; x<x2+1; x++) {
                r = (int) (fr1 + (x - x1 + 1) * ((fr2 - fr1) / (x2 - x1)) - rand + 2 * rand * Math.random());
                g = (int) (fg1 + (x - x1 + 1) * ((fg2 - fg1) / (x2 - x1)) - rand + 2 * rand * Math.random());
                b = (int) (fb1 + (x - x1 + 1) * ((fb2 - fb1) / (x2 - x1)) - rand + 2 * rand * Math.random());
                if (r < 0) r = 0;
                if (r > 255) r = 254;
                if (g < 0) g = 0;
                if (g > 255) g = 254;
                if (b < 0) b = 0;
                if (b > 255) b = 254;

                bm.setPixel(x, y, Color.argb(254, r, g, b));
            }
        }
    }
}