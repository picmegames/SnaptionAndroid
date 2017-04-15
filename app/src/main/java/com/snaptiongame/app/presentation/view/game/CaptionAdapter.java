package com.snaptiongame.app.presentation.view.game;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.Caption;
import com.snaptiongame.app.data.utils.ItemListener;
import com.snaptiongame.app.data.utils.TextStyleUtils;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class CaptionAdapter extends RecyclerView.Adapter {

    private List<Caption> mCaptions;
    private ItemListener mCallback;

    private static final int AVATAR_SIZE = 40;

    public CaptionAdapter(List<Caption> captions) {
        this.mCaptions = captions;

        mCallback = new ItemListener() {
            @Override
            public void updateUpvote(boolean value, int index) {
                mCaptions.get(index).beenUpvoted = value;
                mCaptions.get(index).numVotes = value ? mCaptions.get(index)
                        .numVotes + 1 : mCaptions.get(index).numVotes - 1;
            }

            @Override
            public void updateFlag(boolean value, int index) {
                mCaptions.get(index).beenFlagged = value;
            }
        };
    }

    @Override
    public CaptionCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.caption_card, parent, false);
        return new CaptionCardViewHolder(view, mCallback);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CaptionCardViewHolder holder = (CaptionCardViewHolder) viewHolder;
        Caption curCaption = mCaptions.get(position);

        if (curCaption.creatorPicture != null) {
            Glide.with(holder.mContext)
                    .load(curCaption.creatorPicture)
                    .placeholder(new ColorDrawable(ContextCompat.getColor(holder.mContext, R.color.grey_300)))
                    .dontAnimate()
                    .into(holder.mUserImage);
            holder.imageUrl = curCaption.creatorPicture;
        }
        else {
            holder.mUserImage.setImageDrawable(TextDrawable.builder()
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
        holder.mCaption.setText(TextUtils.concat(curCaption.assocFitB.beforeBlank,
                TextStyleUtils.getTextUnderlined(curCaption.caption),
                curCaption.assocFitB.afterBlank));
        holder.mName.setText(curCaption.creatorName);
        holder.username = curCaption.creatorName;
        holder.mNumberOfUpvotes.setText(String.valueOf(curCaption.numVotes));
        holder.setHasBeenUpvotedOrFlagged(curCaption.beenUpvoted, curCaption.beenFlagged);
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
