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
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.utils.DateUtils;
import com.snaptiongame.app.presentation.view.listeners.ItemListener;
import com.snaptiongame.app.presentation.view.utils.TextStyleUtils;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class WallAdapter extends RecyclerView.Adapter {
    private List<Game> mGames;
    private final ItemListener mCallback;

    private boolean isList = false;
    private int lastPosition = -1;
    private long currentTime;

    private static final int AVATAR_SIZE_GRID = 30;
    private static final int AVATAR_SIZE_LIST = 40;

    public WallAdapter(List<Game> snaptions) {
        this.mGames = snaptions;

        mCallback = new ItemListener() {
            @Override
            public void updateUpvote(boolean value, int index) {
                mGames.get(index).beenUpvoted = value;
                mGames.get(index).numUpvotes = value ? mGames.get(index).numUpvotes + 1 :
                        mGames.get(index).numUpvotes -1;
            }

            @Override
            public void updateFlag(int index, RecyclerView.ViewHolder holder) {
                mGames.remove(index);
                notifyItemRemoved(index);
                Toast.makeText(holder.itemView.getContext(), R.string.flagged, Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    public GameCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(isList ? R.layout.game_card_list : R.layout.game_card_grid, parent, false);
        return new GameCardViewHolder(view, mCallback, isList);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        GameCardViewHolder holder = (GameCardViewHolder) viewHolder;
        Game curGame = mGames.get(position);
        RequestOptions options;

        holder.mGameId = curGame.id;
        holder.mCreatorId = curGame.creatorId;
        holder.mCreator = curGame.creatorName;
        holder.mCreatorImageUrl = curGame.creatorImage;
        holder.isPublic = curGame.isPublic;

        if (holder.isPublic) {
            holder.mPrivateIcon.setVisibility(View.INVISIBLE);
        }
        else {
            holder.mPrivateIcon.setVisibility(View.VISIBLE);
        }

        if (curGame.imageUrl != null) {
            holder.mImage.setAspectRatio((float) curGame.imageWidth / curGame.imageHeight);

            options = new RequestOptions()
                    .priority(Priority.IMMEDIATE)
                    .placeholder(new ColorDrawable(ColorGenerator.MATERIAL.getColor(curGame.imageUrl)));

            Glide.with(holder.mContext)
                    .load(curGame.imageUrl)
                    .apply(options)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.mImage);
            holder.mImageUrl = curGame.imageUrl;
            ViewCompat.setTransitionName(holder.mImage, curGame.imageUrl);
        }
        else {
            Glide.with(holder.mContext)
                    .clear(holder.mImage);
        }

        String creatorName = String.format(holder.mContext.getString(R.string.posted_by), curGame.creatorName);

        if (isList) {
            creatorName = curGame.creatorName;
        }
        holder.mCreatorName.setText(creatorName);

        if (curGame.topCaption != null) {
            holder.mCaptionerImage.setVisibility(View.VISIBLE);
            holder.mCaptionerName.setVisibility(View.VISIBLE);
            holder.mTopCaption.setVisibility(View.VISIBLE);

            if (curGame.topCaption.creatorPicture != null) {

                options = new RequestOptions()
                        .placeholder(new ColorDrawable(ContextCompat.getColor(holder.mContext, R.color.grey_300)))
                        .dontAnimate();

                Glide.with(holder.mContext)
                        .load(curGame.topCaption.creatorPicture)
                        .apply(options)
                        .into(holder.mCaptionerImage);
            }
            else {
                holder.mCaptionerImage.setImageDrawable(TextDrawable.builder()
                        .beginConfig()
                        .width(isList ? AVATAR_SIZE_LIST : AVATAR_SIZE_GRID)
                        .height(isList ? AVATAR_SIZE_LIST : AVATAR_SIZE_GRID)
                        .toUpperCase()
                        .endConfig()
                        .buildRound(curGame.topCaption.creatorName.substring(0, 1),
                                ColorGenerator.MATERIAL.getColor(curGame.topCaption.creatorName)));
            }
            ViewCompat.setTransitionName(holder.mCaptionerImage, holder.mContext.getString(R.string.profile_transition));
            holder.mCaptionerName.setText(curGame.topCaption.creatorName);
            holder.mCaptionerId = curGame.topCaption.creatorId;
            holder.mCaptioner = curGame.topCaption.creatorName;
            holder.mCaptionerImageUrl = curGame.topCaption.creatorPicture;
            holder.mTopCaption.setText(TextUtils.concat(curGame.topCaption.assocFitB.beforeBlank,
                    TextStyleUtils.getTextUnderlined(curGame.topCaption.caption),
                    curGame.topCaption.assocFitB.afterBlank));
        }
        else {
            holder.mCaptionerImage.setVisibility(View.GONE);
            holder.mCaptionerName.setVisibility(View.GONE);
            holder.mTopCaption.setVisibility(View.GONE);
        }

        if (isList) {
            if (curGame.creatorImage != null) {

                options = new RequestOptions()
                        .placeholder(new ColorDrawable(ContextCompat.getColor(holder.mContext, R.color.grey_300)))
                        .dontAnimate();

                Glide.with(holder.mContext)
                        .load(curGame.creatorImage)
                        .apply(options)
                        .into(holder.mCreatorImage);
            }
            else {
                holder.mCreatorImage.setImageDrawable(TextDrawable.builder()
                        .beginConfig()
                        .width(AVATAR_SIZE_GRID)
                        .height(AVATAR_SIZE_GRID)
                        .toUpperCase()
                        .endConfig()
                        .buildRound(curGame.creatorName.substring(0, 1),
                                ColorGenerator.MATERIAL.getColor(curGame.creatorName)));
            }
            ViewCompat.setTransitionName(holder.mCreatorImage, holder.mContext.getString(R.string.profile_transition));

            holder.mTimeLeft.setText(DateUtils.getTimeLeftLabel(holder.mContext, curGame.endDate));
        }

        holder.hasBeenUpvotedOrFlagged(curGame.beenUpvoted);
        holder.mNumberOfUpvotes.setText(String.valueOf(curGame.numUpvotes));

        holder.isClosed = DateUtils.isPastDate(curGame.endDate, currentTime);

        if (holder.isClosed) {
            holder.mGameStatus.setText(holder.mContext.getString(R.string.game_closed));
        }
        else {
            holder.mGameStatus.setText(holder.mContext.getString(R.string.game_open));
        }

        setAnimation(holder.itemView, position);
    }

    public void setIsList(boolean isList) {
        this.isList = isList;
    }

    private void setAnimation(View view, int position) {
        Animation animation = AnimationUtils.loadAnimation(view.getContext(),
                position > lastPosition ? R.anim.up_from_bottom : R.anim.down_from_top);
        view.startAnimation(animation);
        lastPosition = position;
    }

    public void addGames(List<Game> games) {
        int oldSize = mGames.size();
        mGames.addAll(games);
        currentTime = DateUtils.getNow();
        notifyItemRangeInserted(oldSize, mGames.size());
    }

    public void clear() {
        lastPosition = -1;
        int oldSize = mGames.size();
        mGames.clear();
        notifyItemRangeRemoved(0, oldSize);
    }

    public boolean isEmpty() {
        return mGames.isEmpty();
    }

    @Override
    public void onViewDetachedFromWindow(final RecyclerView.ViewHolder holder) {
        ((GameCardViewHolder) holder).itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return mGames.size();
    }
}
