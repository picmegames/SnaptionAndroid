package com.snaptiongame.snaptionapp.presentation.view.game;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.utils.TextStyleUtils;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class CaptionAdapter extends RecyclerView.Adapter {
    private List<Caption> mCaptions;

    public CaptionAdapter(List<Caption> captions) {
        this.mCaptions = captions;
    }

    @Override
    public CaptionCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.caption_card, parent, false);
        return new CaptionCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CaptionCardViewHolder holder = (CaptionCardViewHolder) viewHolder;
        Caption curCaption = mCaptions.get(position);

        if (curCaption.creatorPicture != null) {
            Glide.with(holder.mContext)
                    .load(curCaption.creatorPicture)
                    .into(holder.mUserImage);
        }
        else {
            holder.mUserImage.setImageDrawable(TextDrawable.builder()
                    .beginConfig()
                    .width(40)
                    .height(40)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(curCaption.creatorName.substring(0, 1),
                            ColorGenerator.MATERIAL.getColor(curCaption.creatorName)));
        }
        holder.captionId = curCaption.id;
        holder.mCaption.setText(TextUtils.concat(curCaption.assocFitB.beforeBlank,
                TextStyleUtils.getTextUnderlined(curCaption.caption),
                curCaption.assocFitB.afterBlank));
        holder.mName.setText(curCaption.creatorName);
        holder.mNumberOfLikes.setText(String.valueOf(curCaption.numVotes));
    }

    public void setCaptions(List<Caption> captions) {
        if (!mCaptions.equals(captions)) {
            mCaptions = captions;
            notifyDataSetChanged();
        }
    }

    public void addCaption(Caption caption) {
        mCaptions.add(caption);
        notifyDataSetChanged();
    }

    public List<Caption> getCaptions() {
        return mCaptions;
    }

    @Override
    public int getItemCount() {
        return mCaptions.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
