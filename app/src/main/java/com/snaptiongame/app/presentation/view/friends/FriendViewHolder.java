package com.snaptiongame.app.presentation.view.friends;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.snaptiongame.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Brian Gouldsberry
 */

public class FriendViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.primary_text)
    TextView mName;
    @BindView(R.id.image)
    CircleImageView mImage;
    @BindView(R.id.secondary_text)
    TextView mUsernameField;
    @BindView(R.id.add_remove_friend_icon)
    ImageView mAddRemoveFriendIcon;

    private FriendItemListener mCallback;
    public boolean isSnaptionFriend = false;
    public String friendName;
    public boolean isCurrentUser = false;

    public Context mContext;

    private static final long ANIMATION_DURATION = 150;
    private static final float GONE_SIZE = 0f;
    private static final float HALF_SIZE = 0.5f;
    private static final float FULL_SIZE = 1.0f;

    public FriendViewHolder(View itemView, FriendItemListener callback) {
        super(itemView);
        mContext = itemView.getContext();
        mCallback = callback;
        ButterKnife.bind(this, itemView);

        final ScaleAnimation growAnim = new ScaleAnimation(GONE_SIZE, FULL_SIZE, GONE_SIZE, FULL_SIZE,
                Animation.RELATIVE_TO_SELF, HALF_SIZE, Animation.RELATIVE_TO_SELF, HALF_SIZE);
        final ScaleAnimation shrinkAnim = new ScaleAnimation(FULL_SIZE, GONE_SIZE, FULL_SIZE, GONE_SIZE,
                Animation.RELATIVE_TO_SELF, HALF_SIZE, Animation.RELATIVE_TO_SELF, HALF_SIZE);

        growAnim.setDuration(ANIMATION_DURATION);
        shrinkAnim.setDuration(ANIMATION_DURATION);

        growAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });

        shrinkAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setAddRemoveFriendIcon(isSnaptionFriend);
                mAddRemoveFriendIcon.startAnimation(growAnim);
            }
        });

        mAddRemoveFriendIcon.setOnClickListener(view -> {
            if (isSnaptionFriend) {
                new MaterialDialog.Builder(mContext)
                        .title(mContext.getString(R.string.remove_friend))
                        .content(String.format(mContext.getString(R.string.remove_friend_body), friendName))
                        .positiveText(R.string.yes)
                        .onPositive((materialDialog, dialogAction) -> {
                            mAddRemoveFriendIcon.startAnimation(shrinkAnim);
                            mCallback.setFriendAddRemoveState(friendName, true, getAdapterPosition());
                            isSnaptionFriend = false;
                        })
                        .negativeText(R.string.no)
                        .positiveColor(ContextCompat.getColor(mContext, R.color.colorAccent))
                        .negativeColor(ContextCompat.getColor(mContext, R.color.colorAccent))
                        .show();
            }
            else {
                new MaterialDialog.Builder(mContext)
                        .title(mContext.getString(R.string.add_friend))
                        .content(String.format(mContext.getString(R.string.add_friend_body), friendName))
                        .positiveText(R.string.yes)
                        .onPositive((materialDialog, dialogAction) -> {
                            mAddRemoveFriendIcon.startAnimation(shrinkAnim);
                            mCallback.setFriendAddRemoveState(friendName, false, getAdapterPosition());
                            isSnaptionFriend = true;
                        })
                        .negativeText(R.string.no)
                        .positiveColor(ContextCompat.getColor(mContext, R.color.colorAccent))
                        .negativeColor(ContextCompat.getColor(mContext, R.color.colorAccent))
                        .show();
            }
        });
    }

    public void setAddRemoveFriendIcon(boolean isSnaptionFriend) {
        if (isSnaptionFriend) {
            mAddRemoveFriendIcon.setImageResource(R.drawable.ic_remove_circle_outline_grey_800_24dp);
        }
        else {
            mAddRemoveFriendIcon.setImageResource(R.drawable.ic_person_add_grey_800_24dp);
        }
    }
}
