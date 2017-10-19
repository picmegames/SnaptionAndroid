package com.snaptiongame.app.presentation.view.wall;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.auth.AuthManager;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.models.GameAction;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.data.providers.FacebookShareProvider;
import com.snaptiongame.app.data.providers.GameProvider;
import com.snaptiongame.app.presentation.view.creategame.CreateGameActivity;
import com.snaptiongame.app.presentation.view.customviews.DynamicImageView;
import com.snaptiongame.app.presentation.view.game.GameActivity;
import com.snaptiongame.app.presentation.view.listeners.ItemListener;
import com.snaptiongame.app.presentation.view.login.LoginActivity;
import com.snaptiongame.app.presentation.view.MainActivity;
import com.snaptiongame.app.presentation.view.profile.ProfileActivity;
import com.snaptiongame.app.presentation.view.utils.AnimUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class GameCardViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.image)
    DynamicImageView image;
    @BindView(R.id.top_caption)
    TextView topCaption;
    @BindView(R.id.captioner_image)
    CircleImageView captionerImage;
    @BindView(R.id.captioner_name)
    TextView captionerName;
    @BindView(R.id.creator_name)
    TextView creatorName;
    @BindView(R.id.creator_content)
    RelativeLayout creatorContent;
    @BindView(R.id.upvote)
    ImageView upvoteButton;
    @BindView(R.id.upvote_view)
    LinearLayout upvoteView;
    @BindView(R.id.number_of_upvotes)
    TextView numberOfUpvotes;
    @BindView(R.id.game_status)
    TextView gameStatus;
    @BindView(R.id.private_icon)
    ImageView privateIcon;

    CircleImageView creatorImage;
    TextView timeLeft;

    public Context context;
    public PopupMenu menu;
    public View view;
    private ItemListener listener;

    public int gameId;
    public int creatorId;
    public String creator;
    public String creatorImageUrl;
    public int captionerId;
    public String captioner;
    public String captionerImageUrl;
    public String imageUrl;
    public boolean isClosed;
    public boolean isPublic;
    public boolean isUpvoted = false;

    private static final int CREATOR_ALPHA = 128;

    public GameCardViewHolder(View itemView, ItemListener listener, boolean isList) {
        super(itemView);
        context = itemView.getContext();
        view = itemView;
        ButterKnife.bind(this, itemView);
        this.listener = listener;

        PopupMenu.OnMenuItemClickListener menuItemClickListener = item -> {
            switch (item.getItemId()) {
                case R.id.create_game:
                    if (AuthManager.isLoggedIn()) {
                        startCreateGame();
                    }
                    else {
                        goToLogin();
                    }
                    break;
                case R.id.share:
                    FacebookShareProvider.shareToFacebook((AppCompatActivity) context, image);
                    break;
                case R.id.flag:
                    if (AuthManager.isLoggedIn()) {
                        setBeenFlagged();
                    }
                    else {
                        goToLogin();
                    }
                    break;
                default:
                    break;
            }
            return true;
        };

        if (isList) {
            creatorImage = itemView.findViewById(R.id.creator_image);
            timeLeft = itemView.findViewById(R.id.time_left);

            ImageView moreButton = itemView.findViewById(R.id.more_button);
            menu = new PopupMenu(context, moreButton);
            moreButton.setOnClickListener(view -> {
                menu.setOnMenuItemClickListener(menuItemClickListener);
                menu.show();
            });

            creatorImage.setOnClickListener(view -> goToProfile(creatorId, creator, creatorImageUrl, view));
        }
        else {
            creatorContent.getBackground().setAlpha(CREATOR_ALPHA);

            menu = new PopupMenu(context, image);
            image.setOnLongClickListener(view -> {
                menu.setOnMenuItemClickListener(menuItemClickListener);
                menu.show();
                return true;
            });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            image.setClipToOutline(true);
        }

        final ScaleAnimation growAnimation = AnimUtils.getGrowAnim();
        final ScaleAnimation shrinkAnimation = AnimUtils.getShrinkAnim();

        shrinkAnimation.setAnimationListener(new Animation.AnimationListener() {
            boolean upvoted;

            @Override
            public void onAnimationStart(Animation animation) {
                upvoted = isUpvoted;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                upvoteButton.startAnimation(growAnimation);
            }
        });

        upvoteView.setOnClickListener(view -> {
            if (AuthManager.isLoggedIn()) {
                upvoteButton.startAnimation(shrinkAnimation);
                upvoteGame();
            }
            else {
                goToLogin();
            }
        });

        menu.getMenuInflater().inflate(R.menu.game_menu, menu.getMenu());
        menu.getMenu().findItem(R.id.invite_friend_to_game).setVisible(false);
        menu.getMenu().findItem(R.id.upvote).setVisible(false);
        menu.getMenu().findItem(R.id.tags).setVisible(false);

        image.setOnClickListener(view -> {
            Intent gameIntent = new Intent(context, GameActivity.class);
            gameIntent.putExtra(Game.ID, gameId);
            gameIntent.putExtra(Game.CREATOR_NAME, creator);
            gameIntent.putExtra(Game.CREATOR_IMAGE, creatorImageUrl);
            gameIntent.putExtra(Game.CREATOR_ID, creatorId);
            gameIntent.putExtra(Game.IMAGE_URL, imageUrl);
            gameIntent.putExtra(Game.BEEN_UPVOTED, isUpvoted);
            gameIntent.putExtra(Game.IS_CLOSED, isClosed);
            gameIntent.putExtra(Game.IS_PUBLIC, isPublic);

            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                    .makeSceneTransitionAnimation((AppCompatActivity) context,
                            image, ViewCompat.getTransitionName(image));
            if (context instanceof MainActivity) {
                ((MainActivity) context).setComingFromGameActivity(true);
            }
            context.startActivity(gameIntent, transitionActivityOptions.toBundle());
        });

        captionerImage.setOnClickListener(view ->
                goToProfile(captionerId, captioner, captionerImageUrl, view)
        );
    }

    private void goToLogin() {
        Intent loginIntent = new Intent(context, LoginActivity.class);
        context.startActivity(loginIntent);
    }

    public void hasBeenUpvotedOrFlagged(boolean beenUpvoted) {
        isUpvoted = beenUpvoted;
        setUpvoteIcon(!isUpvoted);
    }

    private void setBeenUpvoted() {
        setUpvoteIcon(isUpvoted);
        if (isUpvoted) {
            isUpvoted = false;
            numberOfUpvotes.setText(String.valueOf(Integer.parseInt(numberOfUpvotes.getText().toString()) - 1));
        }
        else {
            isUpvoted = true;
            numberOfUpvotes.setText(String.valueOf(Integer.parseInt(numberOfUpvotes.getText().toString()) + 1));
            Toast.makeText(context, context.getString(R.string.upvoted), Toast.LENGTH_SHORT).show();
        }
        Timber.i("Successfully updated upvote!");
        listener.updateUpvote(isUpvoted, getAdapterPosition());
    }

    private void setBeenFlagged() {
        new MaterialDialog.Builder(context)
                .title(R.string.flag_alert_game)
                .content(R.string.ask_flag_game)
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .onPositive((@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) -> flagGame())
                .cancelable(true)
                .show();
    }

    private void startCreateGame() {
        Intent createGameIntent = new Intent(context, CreateGameActivity.class);
        createGameIntent.putExtra(Game.GAME_ID, gameId);
        createGameIntent.putExtra(Game.IMAGE_URL, imageUrl);

        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                .makeSceneTransitionAnimation((AppCompatActivity) context,
                        image, ViewCompat.getTransitionName(image));
        ((AppCompatActivity) context).startActivityForResult(createGameIntent,
                MainActivity.WALL_RESULT_CODE, transitionActivityOptions.toBundle());
    }

    private void upvoteGame() {
        GameProvider.upvoteOrFlagGame(new GameAction(gameId, !isUpvoted, GameAction.UPVOTE,
                GameAction.GAME_ID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::setBeenUpvoted,
                        e -> {
                            Toast.makeText(context, context.getString(R.string.upvote_fail), Toast.LENGTH_SHORT).show();
                            Timber.e(e);
                        }
                );
    }

    private void flagGame() {
        GameProvider.upvoteOrFlagGame(new GameAction(gameId, true, GameAction.FLAGGED,
                GameAction.GAME_ID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> listener.updateFlag(getAdapterPosition(), this),
                        e -> {
                            Toast.makeText(context, context.getString(R.string.flagged_fail), Toast.LENGTH_SHORT).show();
                            Timber.e(e);
                        }
                );
    }

    public void unflagGame() {
        GameProvider.upvoteOrFlagGame(new GameAction(gameId, false, GameAction.FLAGGED,
                GameAction.GAME_ID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                        },
                        Timber::e
                );
    }

    private void setUpvoteIcon(boolean isGameUpvoted) {
        if (isGameUpvoted) {
            // upvoteButton.setImageResource(R.drawable.ic_favorite_border_grey_800_24dp);
            upvoteButton.setImageResource(R.drawable.ic_arrow_upward_grey_500_24dp);
            numberOfUpvotes.setTextColor(ContextCompat.getColor(context, R.color.grey_600));
        }
        else {
            // upvoteButton.setImageResource(R.drawable.ic_favorite_pink_300_24dp);
            upvoteButton.setImageResource(R.drawable.ic_arrow_upward_pink_300_24dp);
            numberOfUpvotes.setTextColor(ContextCompat.getColor(context, R.color.pink_300));
        }
    }

    private void goToProfile(int userId, String username, String userImageUrl, View view) {
        Intent profileIntent = new Intent(context, ProfileActivity.class);
        profileIntent.putExtra(User.USERNAME, username);
        profileIntent.putExtra(User.IMAGE_URL, userImageUrl);
        profileIntent.putExtra(User.ID, userId);

        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                .makeSceneTransitionAnimation((AppCompatActivity) context, view,
                        ViewCompat.getTransitionName(view));
        context.startActivity(profileIntent, transitionActivityOptions.toBundle());
    }
}
