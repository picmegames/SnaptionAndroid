package com.snaptiongame.snaptionapp.presentation.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.data.providers.SnaptionProvider;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Nick Romero
 */

public class CreateGame extends AppCompatActivity {
    @BindView(R.id.newGameImage)
    ImageView mNewGameImage;
    @BindView(R.id.createGameUser)
    TextView mUsernameView;
    @BindView(R.id.contentRatingsSpinner)
    Spinner mContentSpinner;
    @BindView(R.id.categorySpinner)
    Spinner mCategorySpinner;

    private Uri mChosenImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        ButterKnife.bind(this);

        assignSpinnerValues();
    }

    @OnClick(R.id.newGameImage)
    public void getImage() {
        Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
        imagePickerIntent.setType("image/*");
        startActivityForResult(imagePickerIntent, 1);
    }

    @OnClick(R.id.createGameButton)
    public void createGame() {
        String username = mUsernameView.getText().toString();

        if (mNewGameImage.getDrawable() != null) {
            byte[] imageByteArray = convertImageToByteArray();

            Caption fakeCaption = new Caption(30, 30, 30, username, 0, "Here is a caption");
            List<Caption> fakeCaptions = new ArrayList<>();
            fakeCaptions.add(0, fakeCaption);

            Snaption newSnaption = new Snaption(30, java.util.Calendar.DATE, java.util.Calendar.DATE,
                  false, username, 30, imageByteArray, null, fakeCaptions);

            // Mock send to server
            SnaptionProvider.testSnaptions.add(0, newSnaption);

            onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mChosenImageUri = data.getData();
            mNewGameImage.setImageURI(mChosenImageUri);
        }
    }

    private void assignSpinnerValues() {
        ArrayAdapter contentAdapter = ArrayAdapter.createFromResource(this,
                R.array.content_ratings_array, android.R.layout.simple_spinner_item);

        ArrayAdapter categoryAdapter =  ArrayAdapter.createFromResource(this,
               R.array.categories_array, android.R.layout.simple_spinner_item);

        contentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mContentSpinner.setAdapter(contentAdapter);
        mCategorySpinner.setAdapter(categoryAdapter);
    }

    private byte[] convertImageToByteArray() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Drawable imageDrawable = mNewGameImage.getDrawable();
        Bitmap bmp = ((BitmapDrawable) imageDrawable).getBitmap();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
