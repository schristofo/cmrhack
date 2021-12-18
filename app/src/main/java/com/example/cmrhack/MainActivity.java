package com.example.cmrhack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.media.ImageReader;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 3);
            }
        });
    }

    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if(reqCode==3 && resultCode == RESULT_OK){
            try {
                Uri imageUri = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ImageView imageView = (ImageView) findViewById(R.id.imageView3);
                imageView.setImageBitmap(selectedImage);
                imageView.setMaxWidth(250);
                imageView.setMaxHeight(250);
            }catch(Exception E){
                E.printStackTrace();
            }
            Toast.makeText(this, "Couldn't load image 1!",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Couldn't load image",Toast.LENGTH_LONG).show();
        }
    }
}