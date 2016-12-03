package com.snaptiongame.snaptionapp.presentation.view.creategame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.snaptiongame.snaptionapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

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

    private static final String REST_ENDPOINT = "http://104.198.36.194/games";

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
        if (mNewGameImage.getDrawable() != null) {
//            convertImageToBase64();
//            Snaption newSnaption = new Snaption("woooo", 0, 0,
//                  false, "", 0, mBase64EncodedImage, null, "image/jpeg", null);
//
//            SnaptionProvider.addSnaption(newSnaption);
            new PostImage().execute();
            onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mNewGameImage.setImageURI(data.getData());
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

    private String convertImageToBase64() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] arr;
        Drawable imageDrawable = mNewGameImage.getDrawable();
        Bitmap bmp = ((BitmapDrawable) imageDrawable).getBitmap();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        arr = byteArrayOutputStream.toByteArray();
        return  Base64.encodeToString(arr, Base64.DEFAULT);
    }

    private class PostImage extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                JSONObject gameJSON = new JSONObject();
                String dataToSend = gameJSON.toString();
                URL url = new URL(REST_ENDPOINT);

                gameJSON.put("pictureEncoded",  convertImageToBase64());
                gameJSON.put("id", "tester");
                gameJSON.put("type", "image/jpeg");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setDoOutput(true);
                connection.setDoInput(true);

                connection.setFixedLengthStreamingMode(dataToSend.getBytes().length);
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

                OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));

                writer.write(dataToSend);
                writer.flush();
                writer.close();
                outputStream.close();
                connection.disconnect();
            }
            catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
