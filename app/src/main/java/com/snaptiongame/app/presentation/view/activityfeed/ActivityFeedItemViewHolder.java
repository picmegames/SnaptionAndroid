package com.snaptiongame.app.presentation.view.activityfeed;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.Caption;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.presentation.view.game.GameActivity;
import com.snaptiongame.app.presentation.view.profile.ProfileActivity;
import com.snaptiongame.app.presentation.view.utils.ActivityFeedUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Tyler Wong
 */

public class ActivityFeedItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.user_image)
    CircleImageView mUserImage;
    @BindView(R.id.activity_message)
    TextView mActivityMessage;
    @BindView(R.id.content_image)
    ImageView mContentImage;

    public Context mContext;
    public View mView;
    public User mFriend;
    public Game mGame;
    public Caption mCaption;

    public ActivityFeedItemViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
    }

    public void setActivityOnClickListener(int type) {
        switch (type) {
            case ActivityFeedUtils.FRIENDED_YOU:
            case ActivityFeedUtils.NEW_FACEBOOK_FRIEND:
                mView.setOnClickListener(view -> goToProfile());
                break;
            case ActivityFeedUtils.CAPTIONED_GAME:
            case ActivityFeedUtils.FRIEND_INVITED_GAME:
            case ActivityFeedUtils.FRIEND_MADE_GAME:
                mView.setOnClickListener(view -> goToGame());
                break;
        }
    }

    private void goToGame() {
        Intent gameIntent = new Intent(mContext, GameActivity.class);
        gameIntent.putExtra(Game.ID, mGame.id);
        gameIntent.putExtra(Game.CREATOR_NAME, mGame.creatorName);
        gameIntent.putExtra(Game.CREATOR_IMAGE, mGame.creatorImage);
        gameIntent.putExtra(Game.CREATOR_ID, mGame.creatorId);
        gameIntent.putExtra(Game.IMAGE_URL, mGame.imageUrl);
        gameIntent.putExtra(Game.BEEN_UPVOTED, mGame.beenUpvoted);
        gameIntent.putExtra(Game.IS_CLOSED, mGame.isClosed);
        gameIntent.putExtra(Game.IS_PUBLIC, mGame.isPublic);

        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                .makeSceneTransitionAnimation((AppCompatActivity) mContext,
                        mContentImage, ViewCompat.getTransitionName(mContentImage));
        mContext.startActivity(gameIntent, transitionActivityOptions.toBundle());
    }

    private void goToProfile() {
        Intent profileIntent = new Intent(mContext, ProfileActivity.class);
        profileIntent.putExtra(User.USERNAME, mFriend.username);
        profileIntent.putExtra(User.IMAGE_URL, mFriend.imageUrl);
        profileIntent.putExtra(User.ID, mFriend.id);

        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                .makeSceneTransitionAnimation((AppCompatActivity) mContext, mUserImage,
                        ViewCompat.getTransitionName(mUserImage));
        mContext.startActivity(profileIntent, transitionActivityOptions.toBundle());
    }
}
