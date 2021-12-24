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

        int red = 0;
        if(reqCode==3 && resultCode == RESULT_OK){
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            try {

                Uri imageUri = data.getData();
                InputStream imageStream = this.getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                Bitmap myBitmap = selectedImage.copy(Bitmap.Config.ARGB_8888, true);
                for(int i=0;i<myBitmap.getHeight();i++){
                    myBitmap.setPixel(10, i,Color.argb(254,0,0,0));
                }

                imageView.setImageBitmap(myBitmap);
                MediaStore.Images.Media.insertImage(this.getContentResolver(), myBitmap,"TempImage", "Best Description Ever");
            }catch(Exception E) {
                Toast.makeText(this, "Couldn't load image",Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(this, "Couldn't load image",Toast.LENGTH_LONG).show();
        }
    }
}