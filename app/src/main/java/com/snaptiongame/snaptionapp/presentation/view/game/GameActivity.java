package com.snaptiongame.snaptionapp.presentation.view.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.caption_list)
    RecyclerView mCaptionList;
    @BindView(R.id.game_image)
    ImageView mImage;
    @BindView(R.id.picker_image)
    ImageView mPickerImage;
    @BindView(R.id.picker_name)
    TextView mPickerName;

    private ActionBar mActionBar;
    private CaptionAdapter mAdapter;
    private AuthenticationManager mAuthManager;
    private GameContract.Presenter mPresenter;
    private CaptionSelectDialogFragment mCaptionDialogFragment;
    private CaptionSelectDialogFragment mCaptionSetDialogFragment;
    private int mGameId;
    private int mPickerId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        mAuthManager = AuthenticationManager.getInstance();

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

        supportPostponeEnterTransition();

        Intent intent = getIntent();

        Glide.with(this)
                .load(intent.getStringExtra("image"))
                .fitCenter()
                .dontAnimate()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }
                })
                .into(mImage);
        mGameId = intent.getIntExtra("gameId", 0);
        mPickerId = intent.getIntExtra("pickerId", 0);
        mPresenter = new GamePresenter(mGameId, mPickerId, this);
        mRefreshLayout.setOnRefreshListener(mPresenter::loadCaptions);

        mPresenter.subscribe();
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void setPickerInfo(String profileUrl, String name) {
        if (profileUrl != null && !profileUrl.isEmpty()) {
            Glide.with(this)
                    .load(profileUrl)
                    .into(mPickerImage);
        }
        else {
            mPickerImage.setImageDrawable(TextDrawable.builder()
                    .beginConfig()
                    .width(40)
                    .height(40)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(name.substring(0, 1),
                            ColorGenerator.MATERIAL.getColor(name)));
        }
        mPickerName.setText(name);
    }

    @Override
    public void setPresenter(GameContract.Presenter presenter) {
        mPresenter = presenter;
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
        mRefreshLayout.setRefreshing(false);
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
