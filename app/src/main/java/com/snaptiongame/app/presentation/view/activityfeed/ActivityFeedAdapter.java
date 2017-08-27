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
import com.bumptech.glide.request.RequestOptions;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.ActivityFeedItem;
import com.snaptiongame.app.data.utils.DateUtils;
import com.snaptiongame.app.presentation.view.utils.ActivityFeedUtils;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class ActivityFeedAdapter extends RecyclerView.Adapter {

    private List<ActivityFeedItem> activityItems;
    private int lastPosition = -1;
    private long currentTime;

    private static final int AVATAR_SIZE = 40;

    public ActivityFeedAdapter(List<ActivityFeedItem> activityItems) {
        this.activityItems = activityItems;
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
        ActivityFeedItem curActivityItem = activityItems.get(position);

        holder.friend = curActivityItem.getFriend();
        holder.game = curActivityItem.getGame();
        holder.caption = curActivityItem.getCaption();

        holder.setActivityOnClickListener(curActivityItem.getType());
        holder.activityMessage.setText(ActivityFeedUtils.getMessage(holder.context, curActivityItem));

        if (curActivityItem.getFriend().getImageUrl() != null) {
            RequestOptions options = new RequestOptions()
                    .placeholder(new ColorDrawable(ContextCompat.getColor(holder.context, R.color.grey_300)))
                    .dontAnimate();

            Glide.with(holder.context)
                    .load(curActivityItem.getFriend().getImageUrl())
                    .apply(options)
                    .into(holder.userImage);
        }
        else {
            holder.userImage.setImageDrawable(TextDrawable.builder()
                    .beginConfig()
                    .width(AVATAR_SIZE)
                    .height(AVATAR_SIZE)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(curActivityItem.getFriend().getUsername().substring(0, 1),
                            ColorGenerator.MATERIAL.getColor(curActivityItem.getFriend().getUsername())));
        }

        if (curActivityItem.getType() != ActivityFeedUtils.FRIENDED_YOU &&
                curActivityItem.getType() != ActivityFeedUtils.NEW_FACEBOOK_FRIEND) {

            holder.contentImage.setVisibility(View.VISIBLE);
            holder.game.setClosed(DateUtils.INSTANCE.isPastDate(holder.game.getEndDate(), currentTime));

            if (curActivityItem.getGame().getImageUrl() != null) {
                RequestOptions options = new RequestOptions()
                        .placeholder(new ColorDrawable(ColorGenerator.MATERIAL.getColor(curActivityItem.getGame().getImageUrl())))
                        .dontAnimate();

                Glide.with(holder.context)
                        .load(curActivityItem.getGame().getImageUrl())
                        .apply(options)
                        .into(holder.contentImage);
                ViewCompat.setTransitionName(holder.contentImage, curActivityItem.getGame().getImageUrl());
            }
            else {
                Glide.with(holder.context)
                        .clear(holder.contentImage);
            }
        }
        else {
            holder.contentImage.setVisibility(View.GONE);
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
        int oldSize = activityItems.size();
        activityItems.addAll(items);
        currentTime = DateUtils.INSTANCE.getNow();
        notifyItemRangeInserted(oldSize, activityItems.size());
    }

    public void clear() {
        lastPosition = -1;
        int oldSize = activityItems.size();
        activityItems.clear();
        notifyItemRangeRemoved(0, oldSize);
    }

    public boolean isEmpty() {
        return activityItems.isEmpty();
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        ((ActivityFeedItemViewHolder) holder).itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return activityItems.size();
    }
}
