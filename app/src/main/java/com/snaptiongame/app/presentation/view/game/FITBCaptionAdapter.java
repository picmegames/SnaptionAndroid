package com.snaptiongame.app.presentation.view.game;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.FitBCaption;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickromero on 2/7/17.
 */

/**
 * A FITBCaptionAdapter is used by the the FITB Dialog View to display sets of empty captions.
 */
public class FITBCaptionAdapter extends RecyclerView.Adapter {
    private List<FitBCaption> mCaptions;
    private CaptionContract.CaptionClickListener mCaptionClickListener;
    private LayoutInflater mInflater;
    private Drawable mOriginalBackground;
    private List<Boolean> areCardsChecked;


    public FITBCaptionAdapter(List<FitBCaption> captions, CaptionContract.CaptionClickListener captionClickListener,
                              LayoutInflater inflater) {
        mCaptions = captions;
        areCardsChecked = new ArrayList<>();

        for (int i = 0; i < captions.size(); i++)
            areCardsChecked.add(false);

        mCaptionClickListener = captionClickListener;
        mInflater = inflater;
    }

    public void setCaptions(List<FitBCaption> captions) {
        this.mCaptions = captions;
        areCardsChecked = new ArrayList<>();
        for (int i = 0; i < captions.size(); i++)
            areCardsChecked.add(false);
        notifyDataSetChanged();
    }

    public FitBCaption getCaption(int index) {
        return mCaptions.get(index);
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

        holder.mCaptionTemplateTextView.setText(curCaption.beforeBlank + curCaption.placeholderText + curCaption.afterBlank);

        holder.mCurrentFitB.setText((position + 1) + "/" + mCaptions.size());

        /**
        holder.mFitBCaptionCard.setOnClickListener(v -> {
            mCaptionClickListener.captionClicked(v, position);

            //Determine which cards are checked and should be highlighted
            if (!areCardsChecked.get(position)) {
                mOriginalBackground = v.getBackground();
                v.setBackgroundResource(R.drawable.card_border_color_pink);
                areCardsChecked.set(position, true);
            }
            else {
                v.setBackground(mOriginalBackground);
                areCardsChecked.set(position, false);
            }
        });*/
    }


    @Override
    public long getItemId(int position) {
        return position;
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