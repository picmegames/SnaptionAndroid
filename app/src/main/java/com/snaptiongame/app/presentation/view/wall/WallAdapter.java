package com.snaptiongame.app.presentation.view.wall;

import android.graphics.drawable.ColorDrawable;
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
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.utils.TextStyleUtils;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class WallAdapter extends RecyclerView.Adapter {
    private List<Game> mGames;

    private int lastPosition = -1;

    private static final int AVATAR_SIZE = 30;
    private static final int MILLIS = 1000;

    public WallAdapter(List<Game> snaptions) {
        this.mGames = snaptions;
    }

    @Override
    public GameCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_card, parent, false);
        return new GameCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        GameCardViewHolder holder = (GameCardViewHolder) viewHolder;
        Game curGame = mGames.get(position);

        holder.mImage.setAspectRatio((float) curGame.imageWidth / curGame.imageHeight);

        holder.mGameId = curGame.id;
        holder.mPickerId = curGame.pickerId;

        if (curGame.imageUrl != null) {
            Glide.with(holder.mContext)
                    .load(curGame.imageUrl)
                    .placeholder(new ColorDrawable(ColorGenerator.MATERIAL.getColor(curGame.imageUrl)))
                    .into(holder.mImage);
            holder.mImageUrl = curGame.imageUrl;
        }
        else {
            Glide.clear(holder.mImage);
        }

        if (curGame.topCaption != null) {
            if (curGame.topCaption.creatorPicture != null) {
                Glide.with(holder.mContext)
                        .load(curGame.topCaption.creatorPicture)
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
            holder.mPickerName.setText(curGame.topCaption.creatorName);
            holder.mTopCaption.setText(TextUtils.concat(curGame.topCaption.assocFitB.beforeBlank,
                    TextStyleUtils.getTextUnderlined(curGame.topCaption.caption),
                    curGame.topCaption.assocFitB.afterBlank));
        }
        else {
            Glide.with(holder.mContext)
                    .load(R.mipmap.ic_launcher)
                    .into(holder.mCaptionerImage);
            holder.mPickerName.setText("");
            holder.mTopCaption.setText(holder.mContext.getString(R.string.default_caption));
        }

        holder.hasBeenUpvotedOrFlagged(curGame.beenUpvoted, curGame.beenFlagged);

        long currentTime = System.currentTimeMillis() / MILLIS;
        if (curGame.endDate - currentTime <= 0) {
            holder.mGameStatus.setText(holder.mContext.getString(R.string.game_closed));
        }
        else {
            holder.mGameStatus.setText(holder.mContext.getString(R.string.game_open));
        }
        setAnimation(holder.itemView, position);
    }

    public void setGames(List<Game> games) {
        if (!mGames.equals(games)) {
            mGames = games;
            notifyDataSetChanged();
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(
                    viewToAnimate.getContext(), (position > lastPosition) ?
                            R.anim.up_from_bottom : R.anim.down_from_top);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
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
