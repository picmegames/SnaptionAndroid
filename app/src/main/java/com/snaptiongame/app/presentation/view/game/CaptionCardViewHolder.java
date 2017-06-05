package com.snaptiongame.app.presentation.view.game;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.auth.AuthManager;
import com.snaptiongame.app.data.models.GameAction;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.data.providers.CaptionProvider;
import com.snaptiongame.app.presentation.view.login.LoginActivity;
import com.snaptiongame.app.presentation.view.profile.ProfileActivity;
import com.snaptiongame.app.presentation.view.utils.AnimUtils;
import com.snaptiongame.app.presentation.view.listeners.ItemListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class CaptionCardViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.user_image)
    CircleImageView mUserImage;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.caption)
    TextView mCaption;
    @BindView(R.id.upvote)
    ImageView mUpvote;
    @BindView(R.id.number_of_upvotes)
    TextView mNumberOfUpvotes;
    @BindView(R.id.upvote_view)
    LinearLayout mUpvoteView;

    public Context mContext;
    public View mView;
    private ItemListener mListener;

    public String imageUrl;
    public String username;
    public int userId;
    public boolean isUpvoted = false;
    public int captionId;

    public CaptionCardViewHolder(View itemView, ItemListener listener) {
        super(itemView);
        mContext = itemView.getContext();
        mView = itemView;
        ButterKnife.bind(this, itemView);
        mListener = listener;

        final ScaleAnimation growAnimation = AnimUtils.getGrowAnim();
        final ScaleAnimation shrinkAnimation = AnimUtils.getShrinkAnim();

        shrinkAnimation.setAnimationListener(new Animation.AnimationListener() {
            boolean upvoted;//Race Condition fix
            @Override
            public void onAnimationStart(Animation animation) {
                upvoted = isUpvoted;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setUpvote(upvoted);
                mUpvote.startAnimation(growAnimation);
            }
        });

        mUpvoteView.setOnClickListener(view -> {
            if (AuthManager.isLoggedIn()) {
                mUpvote.startAnimation(shrinkAnimation);
                upvoteCaption();
            }
            else {
                goToLogin();
            }
        });
        itemView.setOnLongClickListener(view -> {
            if (AuthManager.isLoggedIn()) {
                setBeenFlagged();
            }
            else {
                goToLogin();
            }
            return true;
        });

        mUserImage.setOnClickListener(view -> {
            Intent profileIntent = new Intent(mContext, ProfileActivity.class);
            profileIntent.putExtra(ProfileActivity.IS_CURRENT_USER, false);
            profileIntent.putExtra(User.USERNAME, username);
            profileIntent.putExtra(User.IMAGE_URL, imageUrl);
            profileIntent.putExtra(User.ID, userId);

            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                    .makeSceneTransitionAnimation((AppCompatActivity) mContext, mUserImage,
                            ViewCompat.getTransitionName(mUserImage));
            mContext.startActivity(profileIntent, transitionActivityOptions.toBundle());
        });
    }

    private void goToLogin() {
        Intent loginIntent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(loginIntent);
    }

    public void setHasBeenUpvotedOrFlagged(boolean beenUpvoted) {
        isUpvoted = beenUpvoted;
        setUpvote(!isUpvoted);
    }

    private void setBeenUpvoted() {
        if (isUpvoted) {
            isUpvoted = false;
            mNumberOfUpvotes.setText(String.valueOf(Integer.parseInt(mNumberOfUpvotes.getText().toString()) - 1));
        }
        else {
            isUpvoted = true;
            mNumberOfUpvotes.setText(String.valueOf(Integer.parseInt(mNumberOfUpvotes.getText().toString()) + 1));
            Toast.makeText(mContext, mContext.getString(R.string.upvoted), Toast.LENGTH_SHORT).show();
        }
        mListener.updateUpvote(isUpvoted, getAdapterPosition());
    }

    private void setBeenFlagged() {
        new MaterialDialog.Builder(mContext)
                .title(R.string.flag_alert_caption)
                .content(R.string.ask_flag_caption)
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .onPositive((@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) -> flagCaption())
                .cancelable(true)
                .show();
    }

    private void upvoteCaption() {
        CaptionProvider.upvoteOrFlagCaption(new GameAction(captionId, !isUpvoted, GameAction
                .UPVOTE, GameAction.CAPTION_ID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::setBeenUpvoted,
                        Timber::e
                );
    }

    private void flagCaption() {
        CaptionProvider.upvoteOrFlagCaption(new GameAction(captionId, true, GameAction.FLAGGED,
                GameAction.CAPTION_ID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> mListener.updateFlag(getAdapterPosition(), this),
                        Timber::e
                );
    }

    public void unflagCaption() {
        CaptionProvider.upvoteOrFlagCaption(new GameAction(captionId, false, GameAction.FLAGGED,
                GameAction.CAPTION_ID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {},
                        Timber::e
                );
    }

    private void setUpvote(boolean isGameUpvoted) {
        if (isGameUpvoted) {
            // mUpvote.setImageResource(R.drawable.ic_favorite_border_grey_800_24dp);
            mUpvote.setImageResource(R.drawable.ic_arrow_upward_grey_500_24dp);
            mNumberOfUpvotes.setTextColor(ContextCompat.getColor(mContext, R.color.grey_600));
        }
        else {
            // mUpvote.setImageResource(R.drawable.ic_favorite_pink_300_24dp);
            mUpvote.setImageResource(R.drawable.ic_arrow_upward_pink_300_24dp);
            mNumberOfUpvotes.setTextColor(ContextCompat.getColor(mContext, R.color.pink_300));
        }
    }
}
