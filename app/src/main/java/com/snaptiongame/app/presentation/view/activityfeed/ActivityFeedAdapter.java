package com.snaptiongame.app.presentation.view.activityfeed;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.ActivityFeedItem;
import com.snaptiongame.app.data.utils.DateUtils;
import com.snaptiongame.app.presentation.view.utils.ActivityFeedUtils;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class ActivityFeedAdapter extends RecyclerView.Adapter {

    private List<ActivityFeedItem> mActivityItems;
    private int lastPosition = -1;
    private long currentTime;

    private static final int AVATAR_SIZE = 40;

    public ActivityFeedAdapter(List<ActivityFeedItem> activityItems) {
        mActivityItems = activityItems;
    }

    @Override
    public ActivityFeedItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_feed_item, parent, false);
        return new ActivityFeedItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ActivityFeedItemViewHolder holder = (ActivityFeedItemViewHolder) viewHolder;
        ActivityFeedItem curActivityItem = mActivityItems.get(position);

        holder.mFriend = curActivityItem.friend;
        holder.mGame = curActivityItem.game;
        holder.mCaption = curActivityItem.caption;

        holder.setActivityOnClickListener(curActivityItem.type);
        holder.mActivityMessage.setText(ActivityFeedUtils.getMessage(holder.mContext, curActivityItem));

        if (curActivityItem.friend.imageUrl != null) {
            Glide.with(holder.mContext)
                    .load(curActivityItem.friend.imageUrl)
                    .placeholder(new ColorDrawable(ContextCompat.getColor(holder.mContext, R.color.grey_300)))
                    .dontAnimate()
                    .into(holder.mUserImage);
        }
        else {
            holder.mUserImage.setImageDrawable(TextDrawable.builder()
                    .beginConfig()
                    .width(AVATAR_SIZE)
                    .height(AVATAR_SIZE)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(curActivityItem.friend.username.substring(0, 1),
                            ColorGenerator.MATERIAL.getColor(curActivityItem.friend.username)));
        }

        if (curActivityItem.type != ActivityFeedUtils.FRIENDED_YOU &&
                curActivityItem.type != ActivityFeedUtils.NEW_FACEBOOK_FRIEND) {

            holder.mContentImage.setVisibility(View.VISIBLE);
            holder.mGame.isClosed = DateUtils.isPastDate(holder.mGame.endDate, currentTime);

            if (curActivityItem.game.imageUrl != null) {
                Glide.with(holder.mContext)
                        .load(curActivityItem.game.imageUrl)
                        .placeholder(new ColorDrawable(ColorGenerator.MATERIAL.getColor(curActivityItem.game.imageUrl)))
                        .dontAnimate()
                        .into(holder.mContentImage);
                ViewCompat.setTransitionName(holder.mContentImage, curActivityItem.game.imageUrl);
            }
            else {
                Glide.clear(holder.mContentImage);
            }
        }
        else {
            holder.mContentImage.setVisibility(View.GONE);
        }

        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.fade_in);
            view.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void addActivityItems(List<ActivityFeedItem> items) {
        int oldSize = mActivityItems.size();
        mActivityItems.addAll(items);
        currentTime = DateUtils.getNow();
        notifyItemRangeInserted(oldSize, mActivityItems.size());
    }

    public void clear() {
        lastPosition = -1;
        int oldSize = mActivityItems.size();
        mActivityItems.clear();
        notifyItemRangeRemoved(0, oldSize);
    }

    public boolean isEmpty() {
        return mActivityItems.isEmpty();
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        ((ActivityFeedItemViewHolder) holder).itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return mActivityItems.size();
    }
}
