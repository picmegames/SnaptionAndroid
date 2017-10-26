package com.snaptiongame.app.presentation.view.activityfeed;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.view.Window;
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
    CircleImageView userImage;
    @BindView(R.id.activity_message)
    TextView activityMessage;
    @BindView(R.id.content_image)
    ImageView contentImage;

    public Context context;
    public View view;
    public User friend;
    public Game game;
    public Caption caption;

    public ActivityFeedItemViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
    }

    public void setActivityOnClickListener(int type) {
        switch (type) {
            case ActivityFeedUtils.FRIENDED_YOU:
            case ActivityFeedUtils.NEW_FACEBOOK_FRIEND:
                view.setOnClickListener(view -> goToProfile());
                break;
            case ActivityFeedUtils.CAPTIONED_GAME:
            case ActivityFeedUtils.FRIEND_INVITED_GAME:
            case ActivityFeedUtils.FRIEND_MADE_GAME:
                view.setOnClickListener(view -> goToGame());
                break;
        }
    }

    private void goToGame() {
        Intent gameIntent = new Intent(context, GameActivity.class);
        gameIntent.putExtra(Game.ID, game.getId());
        gameIntent.putExtra(Game.CREATOR_NAME, game.getCreatorName());
        gameIntent.putExtra(Game.CREATOR_IMAGE, game.getCreatorImage());
        gameIntent.putExtra(Game.CREATOR_ID, game.getCreatorId());
        gameIntent.putExtra(Game.IMAGE_URL, game.getImageUrl());
        gameIntent.putExtra(Game.BEEN_UPVOTED, game.getBeenUpvoted());
        gameIntent.putExtra(Game.IS_CLOSED, game.isClosed());
        gameIntent.putExtra(Game.IS_PUBLIC, game.isPublic());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View statusBar = ((AppCompatActivity) context).findViewById(android.R.id.statusBarBackground);
            View navigationBar = ((AppCompatActivity) context).findViewById(android.R.id.navigationBarBackground);

            ActivityOptions transitionActivityOptions = ActivityOptions
                    .makeSceneTransitionAnimation((AppCompatActivity) context,
                            Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME),
                            Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME),
                            Pair.create(contentImage, ViewCompat.getTransitionName(contentImage)));

            context.startActivity(gameIntent, transitionActivityOptions.toBundle());
        }
        else {
            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                    .makeSceneTransitionAnimation((AppCompatActivity) context,
                            contentImage, ViewCompat.getTransitionName(contentImage));
            context.startActivity(gameIntent, transitionActivityOptions.toBundle());
        }
    }

    private void goToProfile() {
        Intent profileIntent = new Intent(context, ProfileActivity.class);
        profileIntent.putExtra(User.USERNAME, friend.getUsername());
        profileIntent.putExtra(User.IMAGE_URL, friend.getImageUrl());
        profileIntent.putExtra(User.ID, friend.getId());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View statusBar = ((AppCompatActivity) context).findViewById(android.R.id.statusBarBackground);
            View navigationBar = ((AppCompatActivity) context).findViewById(android.R.id.navigationBarBackground);

            ActivityOptions transitionActivityOptions = ActivityOptions
                    .makeSceneTransitionAnimation((AppCompatActivity) context,
                            Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME),
                            Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME),
                            Pair.create(userImage, ViewCompat.getTransitionName(userImage)));

            context.startActivity(profileIntent, transitionActivityOptions.toBundle());
        }
        else {
            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                    .makeSceneTransitionAnimation((AppCompatActivity) context, userImage,
                            ViewCompat.getTransitionName(userImage));
            context.startActivity(profileIntent, transitionActivityOptions.toBundle());
        }
    }
}
