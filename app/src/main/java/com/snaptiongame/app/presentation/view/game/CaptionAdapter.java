package com.snaptiongame.app.presentation.view.game;

import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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
import com.bumptech.glide.request.RequestOptions;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.Caption;
import com.snaptiongame.app.presentation.view.listeners.ItemListener;
import com.snaptiongame.app.presentation.view.utils.TextStyleUtils;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class CaptionAdapter extends RecyclerView.Adapter {

    private List<Caption> captions;
    private ItemListener callback;
    private int lastPosition = -1;

    private RecyclerView recyclerView;

    private static final int AVATAR_SIZE = 40;

    public CaptionAdapter(List<Caption> captions, final RecyclerView recyclerView) {
        this.captions = captions;
        this.recyclerView = recyclerView;

        callback = new ItemListener() {
            @Override
            public void updateUpvote(boolean value, int index) {
                CaptionAdapter.this.captions.get(index).beenUpvoted = value;
                CaptionAdapter.this.captions.get(index).numVotes = value ? CaptionAdapter.this.captions.get(index)
                        .numVotes + 1 : CaptionAdapter.this.captions.get(index).numVotes - 1;
            }

            @Override
            public void updateFlag(int index, RecyclerView.ViewHolder holder) {
                final Caption tempCaption = CaptionAdapter.this.captions.remove(index);
                notifyItemRemoved(index);
                Snackbar.make(CaptionAdapter.this.recyclerView, R.string.flagged, Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo, view -> {
                            ((CaptionCardViewHolder) holder).unflagCaption();
                            CaptionAdapter.this.captions.add(index, tempCaption);
                            notifyItemInserted(index);
                        })
                        .show();
            }
        };
    }

    @Override
    public CaptionCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.caption_card, parent, false);
        return new CaptionCardViewHolder(view, callback);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CaptionCardViewHolder holder = (CaptionCardViewHolder) viewHolder;
        Caption curCaption = captions.get(position);

        if (curCaption.creatorPicture != null) {
            RequestOptions options = new RequestOptions()
                    .placeholder(new ColorDrawable(ContextCompat.getColor(holder.context, R.color.grey_300)))
                    .dontAnimate();

            Glide.with(holder.context)
                    .load(curCaption.creatorPicture)
                    .apply(options)
                    .into(holder.userImage);
            holder.imageUrl = curCaption.creatorPicture;
        }
        else {
            holder.userImage.setImageDrawable(TextDrawable.builder()
                    .beginConfig()
                    .width(AVATAR_SIZE)
                    .height(AVATAR_SIZE)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(curCaption.creatorName.substring(0, 1),
                            ColorGenerator.MATERIAL.getColor(curCaption.creatorName)));
        }
        holder.userId = curCaption.creatorId;
        holder.captionId = curCaption.id;
        holder.caption.setText(TextUtils.concat(curCaption.assocFitB.beforeBlank,
                TextStyleUtils.getTextUnderlined(curCaption.caption),
                curCaption.assocFitB.afterBlank));
        holder.name.setText(curCaption.creatorName);
        holder.username = curCaption.creatorName;
        holder.numberOfUpvotes.setText(String.valueOf(curCaption.numVotes));
        holder.setHasBeenUpvotedOrFlagged(curCaption.beenUpvoted);

        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.fade_in);
            view.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        ((CaptionCardViewHolder) holder).itemView.clearAnimation();
    }

    public void addCaptions(List<Caption> captions) {
        int oldSize = this.captions.size();
        this.captions.addAll(captions);
        notifyItemRangeInserted(oldSize, this.captions.size());
    }

    public void clear() {
        lastPosition = -1;
        int oldSize = captions.size();
        captions.clear();
        notifyItemRangeRemoved(0, oldSize);
    }

    public List<Caption> getCaptions() {
        return captions;
    }

    @Override
    public int getItemCount() {
        return captions.size();
    }
}
