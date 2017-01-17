package com.snaptiongame.snaptionapp.presentation.view.game;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.authentication.AuthenticationManager;
import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.providers.CaptionProvider;
import com.snaptiongame.snaptionapp.presentation.view.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * @author Tyler Wong
 */

public class GameActivity extends AppCompatActivity {
   @BindView(R.id.toolbar)
   Toolbar mToolbar;
   @BindView(R.id.fab)
   FloatingActionButton mFab;
   @BindView(R.id.caption_list)
   RecyclerView mCaptionList;
   @BindView(R.id.game_image)
   ImageView mImage;

   private ActionBar mActionBar;
   private CaptionAdapter mAdapter;
   private AuthenticationManager mAuthManager;
   private int mGameId;

   private static final String CAPTIONS_ENDPOINT = "http://104.198.36.194/captions";

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_game);
      ButterKnife.bind(this);

      mAuthManager = AuthenticationManager.getInstance(this);

      LinearLayoutManager layoutManager = new LinearLayoutManager(this);
      layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
      mCaptionList.setLayoutManager(layoutManager);
      mAdapter = new CaptionAdapter(this, new ArrayList<>());
      mCaptionList.setAdapter(mAdapter);

      setSupportActionBar(mToolbar);
      mActionBar = getSupportActionBar();

      if (mActionBar != null) {
         mActionBar.setDisplayHomeAsUpEnabled(true);
         mActionBar.setTitle(getString(R.string.add_caption));
      }

      Intent intent = getIntent();

      Glide.with(this)
            .load(intent.getStringExtra("image"))
            .centerCrop()
            .into(mImage);

      mGameId = intent.getIntExtra("gameId", 0);
   }

   @Override
   protected void onResume() {
      super.onResume();
      loadCaptions();
   }

   @OnClick(R.id.fab)
   public void showAddCaptionDialog() {
      if (!mAuthManager.isLoggedIn()) {
         goToLogin();
      }
      else {
         new MaterialDialog.Builder(this)
               .title(R.string.add_caption)
               .inputType(InputType.TYPE_CLASS_TEXT)
               .input("", "", (@NonNull MaterialDialog dialog, CharSequence input) ->
                     new PostCaptionTask(input.toString(), mGameId).execute()).show();
      }
   }

   private void goToLogin() {
      Intent loginIntent = new Intent(this, LoginActivity.class);
      startActivity(loginIntent);
   }

   private class PostCaptionTask extends AsyncTask<Void, Void, Void> {
      private String input;
      private int gameId;

      public PostCaptionTask(String input, int gameId) {
         this.input = input;
         this.gameId = gameId;
      }

      @Override
      protected Void doInBackground(Void... voids) {
         try {
            JSONObject gameJSON = new JSONObject();
            String dataToSend;
            URL url = new URL(CAPTIONS_ENDPOINT);

            gameJSON.put("message", input);
            gameJSON.put("gameId", gameId);

            dataToSend = gameJSON.toString();

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

      @Override
      protected void onPostExecute(Void aVoid) {
         super.onPostExecute(aVoid);
         //mAdapter.addTempCaption(new Caption(-1, new CaptionMeta(gameId, 0, "", 0, input)));
      }
   }

   private void loadCaptions() {
      CaptionProvider.getCaptions(mGameId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<List<Caption>>() {
               @Override
               public void onCompleted() {

               }

               @Override
               public void onError(Throwable e) {
                  Log.e(TAG, "Nope :(");
               }

               @Override
               public void onNext(List<Caption> captions) {
                  mAdapter.setCaptions(captions);
               }
            });
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case android.R.id.home:
            onBackPressed();
            break;
      }
      return true;
   }
}
