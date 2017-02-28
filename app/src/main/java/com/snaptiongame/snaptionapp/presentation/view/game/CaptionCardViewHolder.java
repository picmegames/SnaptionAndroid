package com.snaptiongame.snaptionapp.presentation.view.game;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.models.Like;
import com.snaptiongame.snaptionapp.data.models.User;
import com.snaptiongame.snaptionapp.data.providers.CaptionProvider;
import com.snaptiongame.snaptionapp.presentation.view.profile.ProfileActivity;

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
    @BindView(R.id.like)
    ImageView mLike;
    @BindView(R.id.flag)
    ImageView mFlag;
    @BindView(R.id.number_of_likes)
    TextView mNumberOfLikes;

    public Context mContext;

    public String imageUrl;
    public String username;
    public int userId;
    public boolean isLiked = false;
    public boolean isFlagged = false;
    public int captionId;

    public CaptionCardViewHolder(View itemView) {
        super(itemView);
        this.mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);

        mLike.setOnClickListener(view -> {
            if (isLiked) {
                mLike.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_border_grey_400_24dp));
                isLiked = false;
                mNumberOfLikes.setText(String.valueOf(Integer.parseInt(mNumberOfLikes.getText().toString()) - 1));
            }
            else {
                mLike.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_red_400_24dp));
                isLiked = true;
                mNumberOfLikes.setText(String.valueOf(Integer.parseInt(mNumberOfLikes.getText().toString()) + 1));
            }
            upvoteCaption(captionId, isLiked);
        });

        mFlag.setOnClickListener(view -> {
            if (isFlagged) {
                mFlag.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_flag_grey_400_24dp));
                isFlagged = false;
            }
            else {
                mFlag.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_flag_black_24dp));
                isFlagged = true;
                Toast.makeText(mContext, "Flagged", Toast.LENGTH_SHORT).show();
            }
            flagCaption(captionId, isFlagged);
        });

        mUserImage.setOnClickListener(view -> {
            Intent profileIntent = new Intent(mContext, ProfileActivity.class);
            profileIntent.putExtra(ProfileActivity.IS_CURRENT_USER, false);
            profileIntent.putExtra(User.USERNAME, username);
            profileIntent.putExtra(User.PICTURE, imageUrl);
            profileIntent.putExtra(User.ID, userId);
            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                    .makeSceneTransitionAnimation((AppCompatActivity) mContext, mUserImage,
                            mContext.getString(R.string.shared_transition));
            mContext.startActivity(profileIntent, transitionActivityOptions.toBundle());
        });
    }

    private void upvoteCaption(int captionId, boolean isLiked) {
        CaptionProvider.upvoteOrFlagCaption(new Like(captionId, isLiked, Like.UPVOTE, Like.CAPTION_ID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        like -> {
                        },
                        Timber::e,
                        () -> Timber.i("Successfully liked caption!")
                );
    }

    private void flagCaption(int captionId, boolean isFlagged) {
        CaptionProvider.upvoteOrFlagCaption(new Like(captionId, isFlagged, Like.FLAGGED, Like.CAPTION_ID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        like -> {
                        },
                        Timber::e,
                        () -> Timber.i("Successfully flagged caption")
                );
    }
}
