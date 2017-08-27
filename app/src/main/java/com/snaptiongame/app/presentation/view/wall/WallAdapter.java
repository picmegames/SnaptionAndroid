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
    private List<Game> games;
    private final ItemListener callback;

    private boolean isList = false;
    private int lastPosition = -1;
    private long currentTime;

    private static final int AVATAR_SIZE_GRID = 30;
    private static final int AVATAR_SIZE_LIST = 40;

    public WallAdapter(List<Game> snaptions) {
        this.games = snaptions;

        callback = new ItemListener() {
            @Override
            public void updateUpvote(boolean value, int index) {
                games.get(index).beenUpvoted = value;
                games.get(index).numUpvotes = value ? games.get(index).numUpvotes + 1 :
                        games.get(index).numUpvotes -1;
            }

            @Override
            public void updateFlag(int index, RecyclerView.ViewHolder holder) {
                games.remove(index);
                notifyItemRemoved(index);
                Toast.makeText(holder.itemView.getContext(), R.string.flagged, Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    public GameCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(isList ? R.layout.game_card_list : R.layout.game_card_grid, parent, false);
        return new GameCardViewHolder(view, callback, isList);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        GameCardViewHolder holder = (GameCardViewHolder) viewHolder;
        Game curGame = games.get(position);
        RequestOptions options;

        holder.gameId = curGame.id;
        holder.creatorId = curGame.creatorId;
        holder.creator = curGame.creatorName;
        holder.creatorImageUrl = curGame.creatorImage;
        holder.isPublic = curGame.isPublic;

        if (holder.isPublic) {
            holder.privateIcon.setVisibility(View.INVISIBLE);
        }
        else {
            holder.privateIcon.setVisibility(View.VISIBLE);
        }

        if (curGame.imageUrl != null) {
            holder.image.setAspectRatio((float) curGame.imageWidth / curGame.imageHeight);

            options = new RequestOptions()
                    .priority(Priority.IMMEDIATE)
                    .placeholder(new ColorDrawable(ColorGenerator.MATERIAL.getColor(curGame.imageUrl)));

            Glide.with(holder.context)
                    .load(curGame.imageUrl)
                    .apply(options)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.image);
            holder.imageUrl = curGame.imageUrl;
            ViewCompat.setTransitionName(holder.image, curGame.imageUrl);
        }
        else {
            Glide.with(holder.context)
                    .clear(holder.image);
        }

        String creatorName = String.format(holder.context.getString(R.string.posted_by), curGame.creatorName);

        if (isList) {
            creatorName = curGame.creatorName;
        }
        holder.creatorName.setText(creatorName);

        if (curGame.topCaption != null) {
            holder.captionerImage.setVisibility(View.VISIBLE);
            holder.captionerName.setVisibility(View.VISIBLE);
            holder.topCaption.setVisibility(View.VISIBLE);

            if (curGame.topCaption.getCreatorPicture() != null) {

                options = new RequestOptions()
                        .placeholder(new ColorDrawable(ContextCompat.getColor(holder.context, R.color.grey_300)))
                        .dontAnimate();

                Glide.with(holder.context)
                        .load(curGame.topCaption.getCreatorPicture())
                        .apply(options)
                        .into(holder.captionerImage);
            }
            else {
                holder.captionerImage.setImageDrawable(TextDrawable.builder()
                        .beginConfig()
                        .width(isList ? AVATAR_SIZE_LIST : AVATAR_SIZE_GRID)
                        .height(isList ? AVATAR_SIZE_LIST : AVATAR_SIZE_GRID)
                        .toUpperCase()
                        .endConfig()
                        .buildRound(curGame.topCaption.getCreatorName().substring(0, 1),
                                ColorGenerator.MATERIAL.getColor(curGame.topCaption.getCreatorName())));
            }
            ViewCompat.setTransitionName(holder.captionerImage, holder.context.getString(R.string.profile_transition));
            holder.captionerName.setText(curGame.topCaption.getCreatorName());
            holder.captionerId = curGame.topCaption.getCreatorId();
            holder.captioner = curGame.topCaption.getCreatorName();
            holder.captionerImageUrl = curGame.topCaption.getCreatorPicture();
            holder.topCaption.setText(TextUtils.concat(curGame.topCaption.getAssocFitB().getBeforeBlank(),
                    TextStyleUtils.getTextUnderlined(curGame.topCaption.getCaption()),
                    curGame.topCaption.getAssocFitB().getAfterBlank()));
        }
        else {
            holder.captionerImage.setVisibility(View.GONE);
            holder.captionerName.setVisibility(View.GONE);
            holder.topCaption.setVisibility(View.GONE);
        }

        if (isList) {
            if (curGame.creatorImage != null) {

                options = new RequestOptions()
                        .placeholder(new ColorDrawable(ContextCompat.getColor(holder.context, R.color.grey_300)))
                        .dontAnimate();

                Glide.with(holder.context)
                        .load(curGame.creatorImage)
                        .apply(options)
                        .into(holder.creatorImage);
            }
            else {
                holder.creatorImage.setImageDrawable(TextDrawable.builder()
                        .beginConfig()
                        .width(AVATAR_SIZE_GRID)
                        .height(AVATAR_SIZE_GRID)
                        .toUpperCase()
                        .endConfig()
                        .buildRound(curGame.creatorName.substring(0, 1),
                                ColorGenerator.MATERIAL.getColor(curGame.creatorName)));
            }
            ViewCompat.setTransitionName(holder.creatorImage, holder.context.getString(R.string.profile_transition));

            holder.timeLeft.setText(DateUtils.INSTANCE.getTimeLeftLabel(holder.context, curGame.endDate));
        }

        holder.hasBeenUpvotedOrFlagged(curGame.beenUpvoted);
        holder.numberOfUpvotes.setText(String.valueOf(curGame.numUpvotes));

        holder.isClosed = DateUtils.INSTANCE.isPastDate(curGame.endDate, currentTime);

        if (holder.isClosed) {
            holder.gameStatus.setText(holder.context.getString(R.string.game_closed));
        }
        else {
            holder.gameStatus.setText(holder.context.getString(R.string.game_open));
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
        int oldSize = this.games.size();
        this.games.addAll(games);
        currentTime = DateUtils.INSTANCE.getNow();
        notifyItemRangeInserted(oldSize, this.games.size());
    }

    public void clear() {
        lastPosition = -1;
        int oldSize = games.size();
        games.clear();
        notifyItemRangeRemoved(0, oldSize);
    }

    public boolean isEmpty() {
        return games.isEmpty();
    }

    @Override
    public void onViewDetachedFromWindow(final RecyclerView.ViewHolder holder) {
        ((GameCardViewHolder) holder).itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return games.size();
    }
}
