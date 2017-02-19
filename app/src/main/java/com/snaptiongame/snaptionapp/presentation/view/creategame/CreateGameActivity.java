package com.snaptiongame.snaptionapp.presentation.view.creategame;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.authentication.AuthenticationManager;
import com.snaptiongame.snaptionapp.data.models.Friend;
import com.snaptiongame.snaptionapp.presentation.view.friends.FriendsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * @author Nick Romero
 */

public class CreateGameActivity extends AppCompatActivity implements CreateGameContract.View {
   @BindView(R.id.layout)
   CoordinatorLayout mLayout;
   @BindView(R.id.toolbar)
   Toolbar mToolbar;
   @BindView(R.id.image)
   ImageView mNewGameImage;
   @BindView(R.id.content_spinner)
   Spinner mContentSpinner;
   @BindView(R.id.private_switch)
   Switch mPrivateSwitch;
   @BindView(R.id.create_game)
   Button mCreateGameButton;
   @BindView(R.id.tag_chip_view)
   NachoTextView mTagTextView;
   @BindView(R.id.add_friends_view)
   RelativeLayout mFriendsView;
   @BindView(R.id.friends_chip_view)
   NachoTextView mFriendsTextView;

   private ActionBar mActionBar;

   private CreateGameContract.Presenter mPresenter;

   private AuthenticationManager mAuthManager;
   private Uri mUri;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_create_game);
      ButterKnife.bind(this);

      mAuthManager = AuthenticationManager.getInstance(this);

      assignValues();

      setSupportActionBar(mToolbar);
      mActionBar = getSupportActionBar();

      if (mActionBar != null) {
         mActionBar.setDisplayHomeAsUpEnabled(true);
         mActionBar.setTitle(getString(R.string.create_game));
      }

      mTagTextView.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
      mTagTextView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
      mTagTextView.addChipTerminator(',', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
      mTagTextView.enableEditChipOnTouch(false, true);
      mFriendsTextView.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
      mFriendsTextView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
      mFriendsTextView.addChipTerminator(',', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
      mFriendsTextView.enableEditChipOnTouch(false, true);

      mPresenter = new CreateGamePresenter(mAuthManager.getSnaptionUserId(), this);
   }

   private void assignValues() {
      ArrayAdapter contentAdapter = ArrayAdapter.createFromResource(this,
            R.array.content_ratings_array, android.R.layout.simple_spinner_item);
      contentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      mContentSpinner.setAdapter(contentAdapter);
   }

   @Override
   public void setPresenter(CreateGameContract.Presenter presenter) {
      mPresenter = presenter;
   }

   @OnClick(R.id.image)
   public void getImage() {
      Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
      imagePickerIntent.setType("image/*");
      startActivityForResult(imagePickerIntent, 1);
   }

   @OnClick(R.id.create_game)
   public void createGame() {
      mPresenter.convertImage(getContentResolver(), mUri, mNewGameImage.getDrawable(),
            mAuthManager.getSnaptionUserId(), !mPrivateSwitch.isChecked());
   }

   @OnCheckedChanged(R.id.private_switch)
   public void switchChanged() {
      if (mPrivateSwitch.isChecked()) {
         mFriendsView.setVisibility(View.VISIBLE);
      }
      else {
         mFriendsView.setVisibility(View.GONE);
      }
   }

   @Override
   public void setFriendNames(String[] friends) {
      ArrayAdapter<String> friendsAdapter = new ArrayAdapter<>(
            this, android.R.layout.simple_expandable_list_item_1, friends);
      mFriendsTextView.setAdapter(friendsAdapter);
   }

   @Override
   public void showCreateFailure() {
      Snackbar.make(mLayout, getString(R.string.create_failure), Snackbar.LENGTH_LONG).show();
   }

   @Override
   public void showCreateSuccess() {
      Toast.makeText(this, getString(R.string.create_success), Toast.LENGTH_LONG).show();
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      if (resultCode == RESULT_OK) {
         mCreateGameButton.setEnabled(true);
         mUri = data.getData();
         Glide.with(this)
               .load(mUri)
               .bitmapTransform(new FitCenter(this))
               .into(mNewGameImage);
      }
   }

   @Override
   public void onResume() {
      super.onResume();
      mPresenter.subscribe();
   }

   @Override
   public void onPause() {
      super.onPause();
      mPresenter.unsubscribe();
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
