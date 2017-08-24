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
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.hootsuite.nachos.ChipConfiguration;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;
import com.hootsuite.nachos.chip.ChipSpan;
import com.hootsuite.nachos.chip.ChipSpanChipCreator;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.hootsuite.nachos.tokenizer.SpanChipTokenizer;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.utils.DateUtils;
import com.snaptiongame.app.presentation.view.customviews.FourThreeImageView;
import com.snaptiongame.app.presentation.view.friends.FriendSearchActivity;
import com.snaptiongame.app.presentation.view.friends.FriendsAdapter;
import com.snaptiongame.app.presentation.view.utils.ShowcaseUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    CoordinatorLayout layout;
    @BindView(R.id.scroll_view)
    NestedScrollView scrollView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image)
    FourThreeImageView newGameImage;
    @BindView(R.id.camera_animation)
    LottieAnimationView animationView;
    @BindView(R.id.private_switch)
    Switch privateSwitch;
    @BindView(R.id.add_friends_button)
    Button addFriendsButton;
    @BindView(R.id.create_game)
    Button createGameButton;
    @BindView(R.id.tags)
    TextView tagsLabel;
    @BindView(R.id.tag_chip_view)
    NachoTextView tagTextView;
    @BindView(R.id.friends_chip_view)
    NachoTextView friendsTextView;
    @BindView(R.id.set_date_field)
    TextView dateLabel;

    private ActionBar actionBar;
    private MaterialDialog progressDialog;
    private MaterialDialog friendsDialog;
    private DatePickerDialog datePickerDialog;
    private FriendsAdapter friendsAdapter;
    private ArrayAdapter<String> friendNameAdapter;

    private CreateGameContract.Presenter presenter;

    private Uri uri;
    private String imageUrl;
    private Calendar calendar;
    private int year;
    private int month;
    private int dayOfMonth;
    private long days;
    private String formattedDate;
    private int gameId = -1;
    private boolean isFromAnotherGame = false;

    private static final String INTENT_TYPE = "image/*";
    private static final String DATE_FORMAT = "MM/dd/yyyy";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        ButterKnife.bind(this);

        presenter = new CreateGamePresenter(this);

        Intent intent = getIntent();
        if (intent.hasExtra(Game.GAME_ID) && intent.hasExtra(Game.IMAGE_URL)) {
            gameId = intent.getIntExtra(Game.GAME_ID, -1);
            imageUrl = intent.getStringExtra(Game.IMAGE_URL);
            isFromAnotherGame = true;
            ViewCompat.setTransitionName(newGameImage, imageUrl);

            RequestOptions options = new RequestOptions()
                    .priority(Priority.IMMEDIATE)
                    .fitCenter()
                    .dontAnimate();

            Glide.with(this)
                    .load(imageUrl)
                    .apply(options)
                    .into(newGameImage);
            createGameButton.setEnabled(true);
        }
        else {
            animationView.setVisibility(View.VISIBLE);
            animationView.setAnimation(getString(R.string.anim_4));
            animationView.playAnimation();
        }

        friendsAdapter = new FriendsAdapter(presenter.getFriends());
        friendsAdapter.setSelectable();

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.create_game));
        }

        friendNameAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_expandable_list_item_1, new ArrayList<>());
        friendsTextView.setAdapter(friendNameAdapter);

        tagTextView.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        tagTextView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        tagTextView.addChipTerminator(',', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        tagTextView.enableEditChipOnTouch(false, true);
        friendsTextView.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        friendsTextView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        friendsTextView.addChipTerminator(',', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        friendsTextView.enableEditChipOnTouch(false, true);
        friendsTextView.setOnChipClickListener((Chip chip, MotionEvent motionEvent) -> {
            int friendId = presenter.getFriendIdByName(chip.getText().toString());
            if (friendId > 0) {
                friendsAdapter.deselectFriend(friendId);
            }
        });
        friendsTextView.setChipTokenizer(new SpanChipTokenizer<>(this, new ChipSpanChipCreator() {
            @Override
            public ChipSpan createChip(@NonNull Context context, @NonNull CharSequence text, Object data) {
                ChipSpan newChip;
                int friendId = presenter.getFriendIdByName(text.toString());

                if (friendId < 0) {
                    newChip = new ChipSpan(context, text,
                            ContextCompat.getDrawable(CreateGameActivity.this, R.drawable.ic_cancel_red_400_24dp), data);
                }
                else {
                    newChip = new ChipSpan(context, text,
                            ContextCompat.getDrawable(CreateGameActivity.this, R.drawable.ic_check_circle_green_400_24dp), data);
                    friendsAdapter.selectFriend(friendId);
                }

                return newChip;
            }

            @Override
            public void configureChip(@NonNull ChipSpan chip, @NonNull ChipConfiguration chipConfiguration) {
                super.configureChip(chip, chipConfiguration);
                chip.setShowIconOnLeft(true);
            }
        }, ChipSpan.class));

        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, DateUtils.TWO_WEEKS_DAYS);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        formattedDate = new SimpleDateFormat(DATE_FORMAT, Locale.US).format(calendar.getTime());
        dateLabel.setText(formattedDate);
        days = DateUtils.TWO_WEEKS / DateUtils.MILLIS;

        // Will only happen once
        showShowcase();
    }

    private void showShowcase() {
        List<View> showcaseViews = new ArrayList<>();
        List<Integer> titles = new ArrayList<>();
        List<Integer> contents = new ArrayList<>();

        if (!isFromAnotherGame) {
            showcaseViews.add(animationView);
            titles.add(R.string.create_game_showcase_title_1);
            contents.add(R.string.create_game_showcase_content_1);
        }

        showcaseViews.add(tagsLabel);
        titles.add(R.string.create_game_showcase_title_2);
        contents.add(R.string.create_game_showcase_content_2);

        showcaseViews.add(privateSwitch);
        titles.add(R.string.create_game_showcase_title_3);
        contents.add(R.string.create_game_showcase_content_3);

        showcaseViews.add(addFriendsButton);
        titles.add(R.string.create_game_showcase_title_4);
        contents.add(R.string.create_game_showcase_content_4);

        showcaseViews.add(dateLabel);
        titles.add(R.string.create_game_showcase_title_5);
        contents.add(R.string.create_game_showcase_content_5);

        ShowcaseUtils.showShowcaseSequence(this, scrollView, showcaseViews, titles, contents);
    }

    @Override
    public void showImageCompressionFailure() {
        progressDialog.dismiss();
        Toast.makeText(this, getString(R.string.compression_error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showUploadFailure() {
        progressDialog.dismiss();
        Toast.makeText(this, getString(R.string.upload_error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showUploadComplete() {
        progressDialog.dismiss();
        setResult(RESULT_OK);
        onBackPressed();
    }

    @Override
    public List<String> getTags() {
        tagTextView.chipifyAllUnterminatedTokens();
        return tagTextView.getChipValues();
    }

    @Override
    public void setPresenter(CreateGameContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public List<String> getAddedFriends() {
        friendsTextView.chipifyAllUnterminatedTokens();
        return friendsTextView.getChipValues();
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
                    == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, 1);
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
        if (friendsDialog == null) {
            showFriendsDialog();
        }
        else {
            friendsDialog.show();
        }
    }

    @Override
    public void showFriendsDialog() {
        if (!presenter.getFriends().isEmpty()) {
            friendsDialog = new MaterialDialog.Builder(this)
                    .title(R.string.add_friends)
                    .adapter(friendsAdapter, new LinearLayoutManager(this))
                    .onPositive((@NonNull MaterialDialog dialog, @NonNull DialogAction which) ->
                            addFriendsToTextView(friendsAdapter.getSelectedFriendNames())
                    )
                    .positiveText(R.string.update)
                    .cancelable(false)
                    .show();
        }
        else {
            new MaterialDialog.Builder(this)
                    .title(R.string.add_friends)
                    .content(R.string.no_friends_message)
                    .onPositive((@NonNull MaterialDialog dialog, @NonNull DialogAction which) ->
                        startActivity(new Intent(this, FriendSearchActivity.class))
                    )
                    .positiveText(R.string.search)
                    .negativeText(R.string.cancel)
                    .cancelable(true)
                    .show();
        }
    }

    private void addFriendsToTextView(List<String> selectedFriendNames) {
        friendsTextView.setText(selectedFriendNames);
    }

    @OnClick(R.id.create_game)
    public void createGame() {
        tagTextView.chipifyAllUnterminatedTokens();
        if (!isFromAnotherGame) {
            presenter.createGame(getContentResolver().getType(uri), uri,
                    !privateSwitch.isChecked(), days);
        }
        else {
            presenter.createGameFromId(gameId, !privateSwitch.isChecked(), days);
        }
        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.upload_title)
                .content(R.string.upload_message)
                .progress(true, 0)
                .cancelable(false)
                .show();
    }

    @OnClick(R.id.set_date_field)
    public void showDatePicker() {
        if (datePickerDialog == null) {
            datePickerDialog = new DatePickerDialog(this, (DatePicker view, int year, int month, int dayOfMonth) -> {
                this.year = year;
                this.month = month;
                this.dayOfMonth = dayOfMonth;
                dateLabel.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                long today = Calendar.getInstance().getTime().getTime();
                Calendar calendar = Calendar.getInstance();
                calendar.set(this.year, this.month, this.dayOfMonth + 1);
                long selectedDay = calendar.getTime().getTime();
                days = selectedDay - today;

                if (days > DateUtils.TWO_WEEKS) {
                    days = DateUtils.TWO_WEEKS / DateUtils.MILLIS;
                }
                else {
                    days /= DateUtils.MILLIS;
                }
            }, year, month, dayOfMonth);

            Calendar calendar = Calendar.getInstance();
            datePickerDialog.getDatePicker().setMinDate(calendar.getTime().getTime());
            calendar.add(Calendar.DATE, DateUtils.TWO_WEEKS_DAYS);
            datePickerDialog.getDatePicker().setMaxDate(calendar.getTime().getTime());
            datePickerDialog.show();
        }
        else {
            datePickerDialog.show();
        }
    }

    @Override
    public void addFriendNames(List<String> friendNames) {
        friendNameAdapter.addAll(friendNames);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            isFromAnotherGame = false;
            animationView.pauseAnimation();
            animationView.setVisibility(View.GONE);
            uri = data.getData();
            createGameButton.setEnabled(true);

            RequestOptions options = new RequestOptions()
                    .fitCenter();

            Glide.with(this)
                    .load(uri)
                    .apply(options)
                    .into(newGameImage);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        animationView.playAnimation();
        friendNameAdapter.clear();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
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
