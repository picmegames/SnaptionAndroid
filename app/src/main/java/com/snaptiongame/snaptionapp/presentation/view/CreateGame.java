package com.snaptiongame.snaptionapp.presentation.view;

import android.content.Intent;
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

import com.snaptiongame.snaptionapp.R;

public class CreateGame extends AppCompatActivity {

    private ImageView newGameImage;

    private Spinner contentSpinner;

    private Spinner categorySpinner;

    private Button createGameButton;

    private CardView imageHolder;

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
            Uri chosenImage = data.getData();
            newGameImage.setImageURI(chosenImage);
/*
            ViewGroup.LayoutParams params = newGameImage.getLayoutParams();

            params.width = wrapContent;
            params.height = matchParent;
            newGameImage.requestLayout();

            imageHolder = (CardView) findViewById(R.id.newGameImageCardHolder);
            ViewGroup.LayoutParams cardParams = imageHolder.getLayoutParams();

            cardParams.width = wrapContent;
            */
            //imageHolder.requestLayout();


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
}
