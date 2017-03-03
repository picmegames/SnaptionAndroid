package com.snaptiongame.app.presentation.view.wall;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.Snaption;
import com.snaptiongame.app.data.utils.TextStyleUtils;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class WallAdapter extends RecyclerView.Adapter {
    private List<Snaption> mSnaptions;

    private static final int AVATAR_SIZE = 30;

    public WallAdapter(List<Snaption> snaptions) {
        this.mSnaptions = snaptions;
    }

    @Override
    public SnaptionCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.snaption_card, parent, false);
        return new SnaptionCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        SnaptionCardViewHolder holder = (SnaptionCardViewHolder) viewHolder;
        Snaption curSnaption = mSnaptions.get(position);

        holder.mGameId = curSnaption.id;
        holder.mPickerId = curSnaption.pickerId;

        if (curSnaption.picture != null) {
            holder.mImage.layout(0, 0, 0, 0);
            Glide.with(holder.mContext)
                    .load(curSnaption.picture)
                    .into(holder.mImage);
            holder.mImageUrl = curSnaption.picture;
        }
        else {
            Glide.clear(holder.mImage);
        }

        if (curSnaption.topCaption != null) {
            if (curSnaption.topCaption.creatorPicture != null) {
                Glide.with(holder.mContext)
                        .load(curSnaption.topCaption.creatorPicture)
                        .into(holder.mCaptionerImage);
            }
            else {
                holder.mCaptionerImage.setImageDrawable(TextDrawable.builder()
                        .beginConfig()
                        .width(AVATAR_SIZE)
                        .height(AVATAR_SIZE)
                        .toUpperCase()
                        .endConfig()
                        .buildRound(curSnaption.topCaption.creatorName.substring(0, 1),
                                ColorGenerator.MATERIAL.getColor(curSnaption.topCaption.creatorName)));
            }
            holder.mPickerName.setText(curSnaption.topCaption.creatorName);
            holder.mTopCaption.setText(TextUtils.concat(curSnaption.topCaption.assocFitB.beforeBlank,
                    TextStyleUtils.getTextUnderlined(curSnaption.topCaption.caption),
                    curSnaption.topCaption.assocFitB.afterBlank));
        }
        else {
            Glide.with(holder.mContext)
                    .load(R.mipmap.ic_launcher)
                    .into(holder.mCaptionerImage);
            holder.mPickerName.setText("");
            holder.mTopCaption.setText(holder.mContext.getString(R.string.default_caption));
        }

        if (curSnaption.endDate != 0) {
            holder.mGameStatus.setText(holder.mContext.getString(R.string.game_closed));
        }
        else {
            holder.mGameStatus.setText(holder.mContext.getString(R.string.game_open));
        }
    }

    public void setSnaptions(List<Snaption> snaptions) {
        if (!mSnaptions.equals(snaptions)) {
            mSnaptions = snaptions;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mSnaptions.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}