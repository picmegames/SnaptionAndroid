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
import com.snaptiongame.app.presentation.view.utils.AnimUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Brian Gouldsberry
 */

public class FriendViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.primary_text)
    TextView name;
    @BindView(R.id.image)
    CircleImageView image;
    @BindView(R.id.secondary_text)
    TextView usernameField;
    @BindView(R.id.add_remove_friend_icon)
    ImageView addRemoveFriendIcon;
    @BindView(R.id.exp)
    TextView exp;

    private FriendItemListener callback;
    public boolean isSnaptionFriend = false;
    public String friendName;
    public boolean isCurrentUser = false;

    public Context context;

    public FriendViewHolder(View itemView, FriendItemListener callback) {
        super(itemView);
        context = itemView.getContext();
        this.callback = callback;
        ButterKnife.bind(this, itemView);

        final ScaleAnimation growAnim = AnimUtils.getGrowAnim();
        final ScaleAnimation shrinkAnim = AnimUtils.getShrinkAnim();

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
                addRemoveFriendIcon.startAnimation(growAnim);
            }
        });

        addRemoveFriendIcon.setOnClickListener(view -> {
            if (isSnaptionFriend) {
                new MaterialDialog.Builder(context)
                        .title(context.getString(R.string.remove_friend))
                        .content(String.format(context.getString(R.string.remove_friend_body), friendName))
                        .positiveText(R.string.yes)
                        .onPositive((materialDialog, dialogAction) -> {
                            addRemoveFriendIcon.startAnimation(shrinkAnim);
                            this.callback.setFriendAddRemoveState(friendName, true, getAdapterPosition());
                            isSnaptionFriend = false;
                        })
                        .negativeText(R.string.no)
                        .positiveColor(ContextCompat.getColor(context, R.color.colorAccent))
                        .negativeColor(ContextCompat.getColor(context, R.color.colorAccent))
                        .show();
            }
            else {
                new MaterialDialog.Builder(context)
                        .title(context.getString(R.string.add_friend))
                        .content(String.format(context.getString(R.string.add_friend_body), friendName))
                        .positiveText(R.string.yes)
                        .onPositive((materialDialog, dialogAction) -> {
                            addRemoveFriendIcon.startAnimation(shrinkAnim);
                            this.callback.setFriendAddRemoveState(friendName, false, getAdapterPosition());
                            isSnaptionFriend = true;
                        })
                        .negativeText(R.string.no)
                        .positiveColor(ContextCompat.getColor(context, R.color.colorAccent))
                        .negativeColor(ContextCompat.getColor(context, R.color.colorAccent))
                        .show();
            }
        });
    }

    public void setAddRemoveFriendIcon(boolean isSnaptionFriend) {
        if (isSnaptionFriend) {
            addRemoveFriendIcon.setImageResource(R.drawable.ic_remove_circle_outline_grey_800_24dp);
        }
        else {
            addRemoveFriendIcon.setImageResource(R.drawable.ic_person_add_grey_800_24dp);
        }
    }
}
