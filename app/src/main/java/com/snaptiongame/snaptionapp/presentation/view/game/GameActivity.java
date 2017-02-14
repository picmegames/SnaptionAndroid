package com.snaptiongame.snaptionapp.presentation.view.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.authentication.AuthenticationManager;
import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.presentation.view.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Tyler Wong
 */

public class GameActivity extends AppCompatActivity implements GameContract.View {
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
   private GameContract.Presenter mPresenter;
   private CaptionSelectDialogFragment mCaptionDialogFragment;
   private CaptionSelectDialogFragment mCaptionSetDialogFragment;
   private int mGameId;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_game);
      ButterKnife.bind(this);
      mAuthManager = AuthenticationManager.getInstance(this);

      LinearLayoutManager layoutManager = new LinearLayoutManager(this);
      layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
      mCaptionList.setLayoutManager(layoutManager);
      mAdapter = new CaptionAdapter(new ArrayList<>());
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
      mPresenter = new GamePresenter(mGameId, this);
   }

   @Override
   public void setPresenter(GameContract.Presenter presenter) {
      mPresenter = presenter;
   }

   @Override
   protected void onResume() {
      super.onResume();
      mPresenter.subscribe();
   }

   @Override
   protected void onPause() {
      super.onPause();
      mPresenter.unsubscribe();
   }

   @Override
   public void addCaption(Caption caption) {
      mAdapter.addCaption(caption);
   }

   @OnClick(R.id.fab)
   public void showAddCaptionDialog() {
      if (!mAuthManager.isLoggedIn()) {
         goToLogin();
      }
      else {
         mCaptionSetDialogFragment = CaptionSelectDialogFragment.newInstance(
                 CaptionSelectDialogFragment.CaptionDialogToShow.SET_CHOOSER,
                 mGameId, -1);
         mCaptionSetDialogFragment.show(getFragmentManager(), "dialog");
      }
   }

   public void displayCaptionChoosingDialog(int setChosen) {
      mCaptionSetDialogFragment.dismiss();
      mCaptionDialogFragment = CaptionSelectDialogFragment.newInstance(
              CaptionSelectDialogFragment.CaptionDialogToShow.CAPTION_CHOOSER,
              mGameId, setChosen);
      mCaptionDialogFragment.show(getFragmentManager(), "dialog");

   }

   public void negativeButtonClicked(CaptionSelectDialogFragment.CaptionDialogToShow whichDialog) {

      if (mCaptionDialogFragment != null)
         mCaptionDialogFragment.dismiss();


      if (whichDialog == CaptionSelectDialogFragment.CaptionDialogToShow.SET_CHOOSER) {
         if (mCaptionSetDialogFragment != null)
            mCaptionSetDialogFragment.dismiss();
      }
      else {

         mCaptionSetDialogFragment.show(getFragmentManager(), "dialog");
      }

   }

   private void goToLogin() {
      Intent loginIntent = new Intent(this, LoginActivity.class);
      startActivity(loginIntent);
   }

   @Override
   public void showCaptions(List<Caption> captions) {
      mAdapter.setCaptions(captions);
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
