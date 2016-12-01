package com.snaptiongame.snaptionapp.presentation.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.graphics.drawable.BitmapDrawable;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.snaptiongame.snaptionapp.Manifest;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.models.Snaption;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class CreateGame extends AppCompatActivity {

    private ImageView newGameImage;

    private Spinner contentSpinner;

    private Spinner categorySpinner;

    private Button createGameButton;

    private CardView imageHolder;

    private Uri chosenImageURI;

    //private  URI chosenImageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        newGameImage = (ImageView) findViewById(R.id.newGameImage);
        newGameImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
                imagePickerIntent.setType("image/*");
                startActivityForResult(imagePickerIntent, 1);
            }
        });

        createGameButton = (Button) findViewById(R.id.createGameButton);
        createGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = findViewById(R.id.createGameUser).toString();
                String contentLevel = contentSpinner.getSelectedItem().toString();
                String category = categorySpinner.getSelectedItem().toString();

                if (newGameImage.getDrawable() != null) {
                    byte[] imageByteArray = convertImageToByteArray();

                    Caption fakeCaption = new Caption(30, 30, 30,  username, 0, "Here is a caption");
                    List<Caption> fakeCaptions = new ArrayList<Caption>();
                    fakeCaptions.add(0,fakeCaption);

                    
                    Snaption newSnaption = new Snaption(30, java.util.Calendar.DATE, java.util.Calendar.DATE,
                            false, username, 30, imageByteArray, "null", fakeCaptions);


                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("Snaption", newSnaption);
                    setResult(2, resultIntent);
                    finish();

                }

            }
        });

        assignSpinnerValues();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        int wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT;
        int matchParent = ViewGroup.LayoutParams.MATCH_PARENT;

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {

            chosenImageURI = data.getData();
            newGameImage.setImageURI(chosenImageURI);

        }
    }

    private void assignSpinnerValues() {


        contentSpinner = (Spinner) findViewById(R.id.contentRatingsSpinner);
        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);


        ArrayAdapter contentAdapter = ArrayAdapter.createFromResource(this,
                R.array.content_ratings_array, android.R.layout.simple_spinner_item);

        ArrayAdapter categoryAdapter =  ArrayAdapter.createFromResource(this,
               R.array.categories_array, android.R.layout.simple_spinner_item);

        contentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        contentSpinner.setAdapter(contentAdapter);
        categorySpinner.setAdapter(categoryAdapter);
    }

    private byte[] convertImageToByteArray() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        URI javaURI = null;
        FileInputStream fileInputStream = null;
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        Drawable imageDrawable = newGameImage.getDrawable();
        Bitmap bmp = ((BitmapDrawable)imageDrawable).getBitmap();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        buffer = byteArrayOutputStream.toByteArray();
        return buffer;



        //return buffer;
    }
}
