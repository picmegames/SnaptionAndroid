package com.snaptiongame.app.presentation.view.creategame;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.hootsuite.nachos.ChipConfiguration;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.ChipSpan;
import com.hootsuite.nachos.chip.ChipSpanChipCreator;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.hootsuite.nachos.tokenizer.SpanChipTokenizer;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.authentication.AuthenticationManager;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.presentation.view.friends.FriendsAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.friends_chip_view)
    NachoTextView mFriendsTextView;
    @BindView(R.id.set_date_field)
    TextView mDateLabel;

    private ActionBar mActionBar;
    private MaterialDialog mProgressDialog;
    private MaterialDialog mFriendsDialog;
    private FriendsAdapter mFriendsAdapter;

    private CreateGameContract.Presenter mPresenter;

    private AuthenticationManager mAuthManager;
    private Uri mUri;
    private Calendar mCalendar;
    private int mYear;
    private int mMonth;
    private int mDayOfMonth;
    private String mFormattedDate;

    private static final String INTENT_TYPE = "image/*";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        ButterKnife.bind(this);

        mAuthManager = AuthenticationManager.getInstance();
        mPresenter = new CreateGamePresenter(mAuthManager.getUserId(), this);

        Intent intent = getIntent();
        if (intent.hasExtra(Game.PICTURE)) {
            Glide.with(this)
                    .load(intent.getStringExtra(Game.PICTURE))
                    .fitCenter()
                    .into(mNewGameImage);
            mCreateGameButton.setEnabled(true);
        }

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
        mFriendsTextView.setChipTokenizer(new SpanChipTokenizer<>(this, new ChipSpanChipCreator() {
            @Override
            public ChipSpan createChip(@NonNull Context context, @NonNull CharSequence text, Object data) {
                ChipSpan newChip;

                if (mPresenter.getFriendIdByName(text.toString()) < 0) {
                    newChip = new ChipSpan(context, text,
                            ContextCompat.getDrawable(CreateGameActivity.this, R.drawable.ic_cancel_red_400_24dp), data);
                }
                else {
                    newChip = new ChipSpan(context, text,
                            ContextCompat.getDrawable(CreateGameActivity.this, R.drawable.ic_check_circle_green_400_24dp), data);
                }

                return newChip;
            }

            @Override
            public void configureChip(@NonNull ChipSpan chip, @NonNull ChipConfiguration chipConfiguration) {
                super.configureChip(chip, chipConfiguration);
                chip.setShowIconOnLeft(true);
            }
        }, ChipSpan.class));

        mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DATE, 1);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);
        mFormattedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(mCalendar.getTime());
        mDateLabel.setText(mFormattedDate);
    }

    private void assignValues() {
        ArrayAdapter contentAdapter = ArrayAdapter.createFromResource(this,
                R.array.content_ratings_array, android.R.layout.simple_spinner_item);
        contentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mContentSpinner.setAdapter(contentAdapter);
    }

    @Override
    public void showUploadComplete() {
        mProgressDialog.dismiss();
        onBackPressed();
    }

    @Override
    public List<String> getTags() {
        return mTagTextView.getChipValues();
    }

    @Override
    public void setPresenter(CreateGameContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public List<String> getAddedFriends() {
        return mFriendsTextView.getChipValues();
    }

    @Override
    public Context getContext() {
        return CreateGameActivity.this;
    }

    @OnClick(R.id.image)
    public void getImage() {
        if (isStoragePermissionGranted()) {
            pickImage();
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            return true;
        }
    }

    private void pickImage() {
        Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
        imagePickerIntent.setType(INTENT_TYPE);
        startActivityForResult(imagePickerIntent, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        }
    }

    @OnClick(R.id.add_friends_button)
    public void prepareFriendsDialog() {
        if (mFriendsDialog == null) {
            mPresenter.loadFriends();
        }
        else {
            mFriendsDialog.show();
        }
    }

    @Override
    public void showFriendsDialog() {
        mFriendsAdapter = new FriendsAdapter(mPresenter.getFriends());
        mFriendsAdapter.setSelectable();

        mFriendsDialog = new MaterialDialog.Builder(this)
                .title(R.string.add_friends)
                .adapter(mFriendsAdapter, new LinearLayoutManager(this))
                .onPositive((@NonNull MaterialDialog dialog, @NonNull DialogAction which) ->
                        addFriendsToTextView(mFriendsAdapter.getSelectedFriendNames())
                )
                .positiveText(R.string.update)
                .cancelable(false)
                .show();
    }

    private void addFriendsToTextView(List<String> selectedFriendNames) {
        mFriendsTextView.setText(selectedFriendNames);
    }

    @OnClick(R.id.create_game)
    public void createGame() {
        mPresenter.createGame(getContentResolver().getType(mUri), mUri, mAuthManager.getUserId(),
                !mPrivateSwitch.isChecked());
        mProgressDialog = new MaterialDialog.Builder(this)
                .title(R.string.upload_title)
                .content(R.string.upload_message)
                .progress(true, 0)
                .cancelable(false)
                .show();
    }

    @OnClick(R.id.set_date_field)
    public void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (DatePicker view, int year, int month, int dayOfMonth) -> {
            mYear = year;
            mMonth = month;
            mDayOfMonth = dayOfMonth;
            mDateLabel.setText((month + 1) + "/" + dayOfMonth + "/" + year);
        }, mYear, mMonth, mDayOfMonth);
        datePickerDialog.show();
    }

    @Override
    public void setFriendNames(String[] friends) {
        ArrayAdapter<String> friendsAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_expandable_list_item_1, friends);
        mFriendsTextView.setAdapter(friendsAdapter);
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
