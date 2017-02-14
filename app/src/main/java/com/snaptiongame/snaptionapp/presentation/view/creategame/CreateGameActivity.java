package com.snaptiongame.snaptionapp.presentation.view.creategame;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.authentication.AuthenticationManager;
import com.snaptiongame.snaptionapp.data.models.Friend;
import com.snaptiongame.snaptionapp.presentation.view.friends.FriendsAdapter;
import com.snaptiongame.snaptionapp.presentation.view.friends.FriendsTouchListener;

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
   @BindView(R.id.image)
   ImageView mNewGameImage;
   @BindView(R.id.content_spinner)
   Spinner mContentSpinner;
   @BindView(R.id.category_spinner)
   Spinner mCategorySpinner;
   @BindView(R.id.public_switch)
   Switch mPublicSwitch;
   @BindView(R.id.add_friends)
   Button mAddFriendsButton;

   private CreateGameContract.Presenter mPresenter;

   private MaterialDialog mAddFriendsDialog;
   private FriendsAdapter mAdapter;
   private LinearLayoutManager mLayoutManager;

   private AuthenticationManager mAuthManager;
   private Uri mUri;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_create_game);
      ButterKnife.bind(this);

      mAuthManager = AuthenticationManager.getInstance(this);

      assignSpinnerValues();

      mAdapter = new FriendsAdapter(new ArrayList<>());
      mAdapter.setSelectable();
      mLayoutManager = new LinearLayoutManager(this);

      mPresenter = new CreateGamePresenter(mAuthManager.getSnaptionUserId(), this);
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
            mAuthManager.getSnaptionUserId(), !mPublicSwitch.isChecked());
   }

   @OnClick(R.id.add_friends)
   public void addFriends() {
      mAddFriendsDialog = new MaterialDialog.Builder(this)
            .title(R.string.add_friends)
            .adapter(mAdapter, mLayoutManager)
            .positiveText(R.string.ok)
            .onPositive((@NonNull MaterialDialog dialog, @NonNull DialogAction which) ->
               Toast.makeText(this, R.string.friends_added, Toast.LENGTH_LONG).show()
            )
            .cancelable(true)
            .show();
      mAddFriendsDialog.getRecyclerView().addOnItemTouchListener(
            new FriendsTouchListener(this, mAdapter));
   }

   @OnCheckedChanged(R.id.public_switch)
   public void switchChanged() {
      if (mPublicSwitch.isChecked()) {
         mAddFriendsButton.setVisibility(View.VISIBLE);
      }
      else {
         mAddFriendsButton.setVisibility(View.GONE);
      }
   }

   @Override
   public void setFriends(List<Friend> friends) {
      mAdapter.setFriends(friends);
      mAdapter.notifyDataSetChanged();
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
         mAddFriendsButton.setEnabled(true);
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
   public void onBackPressed() {
      super.onBackPressed();
   }

   private void assignSpinnerValues() {
      ArrayAdapter contentAdapter = ArrayAdapter.createFromResource(this,
            R.array.content_ratings_array, android.R.layout.simple_spinner_item);

      ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(this,
            R.array.categories_array, android.R.layout.simple_spinner_item);

      contentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

      mContentSpinner.setAdapter(contentAdapter);
      mCategorySpinner.setAdapter(categoryAdapter);
   }
}
