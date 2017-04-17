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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.auth.AuthManager;
import com.snaptiongame.app.data.models.GameAction;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.data.providers.CaptionProvider;
import com.snaptiongame.app.data.utils.ItemListener;
import com.snaptiongame.app.presentation.view.login.LoginActivity;
import com.snaptiongame.app.presentation.view.profile.ProfileActivity;

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
    @BindView(R.id.flag)
    ImageView mFlag;
    @BindView(R.id.number_of_upvotes)
    TextView mNumberOfUpvotes;

    public Context mContext;
    public View mView;
    private ItemListener mListener;

    public String imageUrl;
    public String username;
    public int userId;
    public boolean isUpvoted = false;
    public boolean isFlagged = false;
    public int captionId;

    public CaptionCardViewHolder(View itemView, ItemListener listener) {
        super(itemView);
        mContext = itemView.getContext();
        mView = itemView;
        ButterKnife.bind(this, itemView);
        mListener = listener;

        mUpvote.setOnClickListener(view -> {
            if (AuthManager.isLoggedIn()) {
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

        itemView.setOnClickListener(view -> {
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

    public void setHasBeenUpvotedOrFlagged(boolean beenUpvoted, boolean beenFlagged) {
        isUpvoted = beenUpvoted;
        isFlagged = beenFlagged;

        if (isUpvoted) {
            mUpvote.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_pink_300_24dp));
        }
        else {
            mUpvote.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_border_grey_800_24dp));
        }
        if (isFlagged) {
            mFlag.setVisibility(View.VISIBLE);
        }
        else {
            mFlag.setVisibility(View.INVISIBLE);
        }
    }

    private void setBeenUpvoted() {
        if (isUpvoted) {
            mUpvote.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_border_grey_800_24dp));
            isUpvoted = false;
            mNumberOfUpvotes.setText(String.valueOf(Integer.parseInt(mNumberOfUpvotes.getText().toString()) - 1));
        }
        else {
            mUpvote.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_pink_300_24dp));
            isUpvoted = true;
            mNumberOfUpvotes.setText(String.valueOf(Integer.parseInt(mNumberOfUpvotes.getText().toString()) + 1));
            Toast.makeText(mContext, mContext.getString(R.string.upvoted), Toast.LENGTH_LONG).show();
        }
        mListener.updateUpvote(isUpvoted, getAdapterPosition());
    }

    private void setBeenFlagged() {
        if (isFlagged) {
            flagCaption();
        }
        else {
            new MaterialDialog.Builder(mContext)
                    .title(R.string.flag_alert_caption)
                    .content(R.string.ask_flag_caption)
                    .positiveText(R.string.confirm)
                    .negativeText(R.string.cancel)
                    .onPositive((@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) -> flagCaption())
                    .cancelable(true)
                    .show();
        }
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
        CaptionProvider.upvoteOrFlagCaption(new GameAction(captionId, !isFlagged, GameAction
                .FLAGGED, GameAction.CAPTION_ID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            if (isFlagged) {
                                mFlag.setVisibility(View.INVISIBLE);
                                isFlagged = false;
                                Toast.makeText(mContext, R.string.unflagged, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                mFlag.setVisibility(View.VISIBLE);
                                isFlagged = true;
                                Toast.makeText(mContext, R.string.flagged, Toast.LENGTH_SHORT).show();
                            }
                            mListener.updateFlag(isFlagged, getAdapterPosition());
                        },
                        Timber::e
                );
    }
}
