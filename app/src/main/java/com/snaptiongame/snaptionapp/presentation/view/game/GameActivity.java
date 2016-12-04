package com.snaptiongame.snaptionapp.presentation.view.game;

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
import com.snaptiongame.snaptionapp.data.models.Caption;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_game);
      ButterKnife.bind(this);

      LinearLayoutManager layoutManager = new LinearLayoutManager(this);
      layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
      mCaptionList.setLayoutManager(layoutManager);

      List<Caption> mockCaptions = new ArrayList<>();
      mockCaptions.add(new Caption(0, 0, 0, "test_captioner", 1000, "It's working!"));
      mockCaptions.add(new Caption(1, 1, 1, "Jacob", 2, "This is awesome!"));
      mockCaptions.add(new Caption(2, 2, 2, "Javon", 4, "Woohoo!"));
      mockCaptions.add(new Caption(3, 3, 3, "Brian", 3, "What a caption."));
      mockCaptions.add(new Caption(4, 4, 4, "Nick", 5, "Is this working?"));
      mockCaptions.add(new Caption(5, 5, 5, "Quang", 10, "Let's go!"));

      mCaptionList.setAdapter(new CaptionAdapter(this, mockCaptions));

      setSupportActionBar(mToolbar);
      mActionBar = getSupportActionBar();

      if (mActionBar != null) {
         mActionBar.setDisplayHomeAsUpEnabled(true);
         mActionBar.setTitle(getString(R.string.add_caption));
      }

      Glide.with(this)
            .load(getIntent().getStringExtra("image"))
            .centerCrop()
            .into(mImage);
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
