package com.snaptiongame.app.presentation.view.wall;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.utils.ItemListener;
import com.snaptiongame.app.data.utils.TextStyleUtils;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class WallAdapter extends RecyclerView.Adapter {
    private List<Game> mGames;
    private final ItemListener mCallback;

    private int lastPosition = -1;
    private long currentTime;

    private static final int AVATAR_SIZE = 30;
    private static final int MILLIS = 1000;

    public WallAdapter(List<Game> snaptions) {
        this.mGames = snaptions;

        mCallback = new ItemListener() {
            @Override
            public void updateUpvote(boolean value, int index) {
                mGames.get(index).beenUpvoted = value;
            }

            @Override
            public void updateFlag(boolean value, int index) {
                mGames.get(index).beenFlagged = value;
            }
        };
    }

    @Override
    public GameCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_card, parent, false);
        return new GameCardViewHolder(view, mCallback);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        GameCardViewHolder holder = (GameCardViewHolder) viewHolder;
        Game curGame = mGames.get(position);

        holder.mGameId = curGame.id;
        holder.mPickerId = curGame.pickerId;

        if (curGame.imageUrl != null) {
            holder.mImage.setAspectRatio((float) curGame.imageWidth / curGame.imageHeight);
            Glide.with(holder.mContext)
                    .load(curGame.imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .priority(Priority.IMMEDIATE)
                    .placeholder(new ColorDrawable(ColorGenerator.MATERIAL.getColor(curGame.imageUrl)))
                    .into(holder.mImage);
            holder.mImageUrl = curGame.imageUrl;
            ViewCompat.setTransitionName(holder.mImage, curGame.imageUrl);
        }
        else {
            Glide.clear(holder.mImage);
        }

        if (curGame.topCaption != null) {
            holder.mCaptionerImage.setVisibility(View.VISIBLE);
            holder.mCaptionerName.setVisibility(View.VISIBLE);

            if (curGame.topCaption.creatorPicture != null) {
                Glide.with(holder.mContext)
                        .load(curGame.topCaption.creatorPicture)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(new ColorDrawable(ContextCompat.getColor(holder.mContext, R.color.grey_300)))
                        .dontAnimate()
                        .into(holder.mCaptionerImage);
            }
            else {
                holder.mCaptionerImage.setImageDrawable(TextDrawable.builder()
                        .beginConfig()
                        .width(AVATAR_SIZE)
                        .height(AVATAR_SIZE)
                        .toUpperCase()
                        .endConfig()
                        .buildRound(curGame.topCaption.creatorName.substring(0, 1),
                                ColorGenerator.MATERIAL.getColor(curGame.topCaption.creatorName)));
            }
            holder.mCaptionerName.setText(curGame.topCaption.creatorName);
            holder.mTopCaption.setText(TextUtils.concat(curGame.topCaption.assocFitB.beforeBlank,
                    TextStyleUtils.getTextUnderlined(curGame.topCaption.caption),
                    curGame.topCaption.assocFitB.afterBlank));
        }
        else {
            holder.mCaptionerImage.setVisibility(View.GONE);
            holder.mCaptionerName.setVisibility(View.GONE);
            holder.mTopCaption.setText(holder.mContext.getString(R.string.default_caption));
        }

        holder.hasBeenUpvotedOrFlagged(curGame.beenUpvoted, curGame.beenFlagged);

        if (curGame.endDate - currentTime <= 0) {
            holder.mGameStatus.setText(holder.mContext.getString(R.string.game_closed));
        }
        else {
            holder.mGameStatus.setText(holder.mContext.getString(R.string.game_open));
        }

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(),
                (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = position;
    }

    public void setGames(List<Game> games) {
        if (!mGames.equals(games)) {
            mGames = games;
            currentTime = System.currentTimeMillis() / MILLIS;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onViewDetachedFromWindow(final RecyclerView.ViewHolder holder) {
        ((GameCardViewHolder) holder).itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return mGames.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
