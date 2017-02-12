package com.snaptiongame.snaptionapp.presentation.view.game;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.models.FitBCaption;

import java.util.List;

/**
 * Created by nickromero on 2/7/17.
 */

public class FITBCaptionAdapter extends RecyclerView.Adapter {
    private List<FitBCaption> mCaptions;

    public FITBCaptionAdapter(List<FitBCaption> captions) {
        mCaptions = captions;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fitb_caption_card, parent, false);
        return new FITBCaptionCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        FITBCaptionCardViewHolder holder = (FITBCaptionCardViewHolder) viewHolder;
        FitBCaption curCaption = mCaptions.get(position);


        holder.mCaptionTemplate = curCaption.beforeBlank + "_______" + curCaption.afterBlank;
        holder.mCaptionTemplateTextView.setText(holder.mCaptionTemplate);
        holder.mCurFitB = String.valueOf(position);
        holder.mCurrentFitB.setText((position + 1) + "/" + mCaptions.size());


    }


    public void addTempCaption(FitBCaption caption) {
        mCaptions.add(caption);
        notifyDataSetChanged();
    }

    public void setCaptions(List<FitBCaption> captions) {
        this.mCaptions = captions;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mCaptions.size();
    }

    public void clearCaptions() {
        mCaptions.clear();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
