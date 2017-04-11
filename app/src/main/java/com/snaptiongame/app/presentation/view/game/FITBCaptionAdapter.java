package com.snaptiongame.app.presentation.view.game;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.FitBCaption;

import java.util.ArrayList;
import java.util.Arrays;
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
    private FitBCaption mSelectedCaption;

    private int lastPosition = -1;

    private static final int RESTING_ELEVATION = 2;
    private static final int SELECTED_ELEVATION = 8;

    public FITBCaptionAdapter(List<FitBCaption> captions, CaptionContract.CaptionClickListener captionClickListener) {
        mCaptions = captions;
        mCaptionClickListener = captionClickListener;
    }

    public void setCaptions(List<FitBCaption> captions) {
        this.mCaptions = captions;
        notifyDataSetChanged();
    }

    public void resetCaption() {
        notifyItemChanged(mCaptions.indexOf(mSelectedCaption));
        mSelectedCaption = null;
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

        String fitbText = curCaption.beforeBlank + curCaption.placeholderText + curCaption.afterBlank;
        ArrayList<String> fitbs = new ArrayList<>(
                Arrays.asList(curCaption.beforeBlank,
                        curCaption.placeholderText,
                        curCaption.afterBlank)
        );

        holder.mCaptionTemplateTextView.setText(fitbText);

        holder.mCurrentFitB.setText((position + 1) + "/" + mCaptions.size());

        holder.mFitBCaptionCard.setOnClickListener(v -> {
            mCaptionClickListener.captionClicked(v, holder.getAdapterPosition(), fitbs);
            int oldSelectedCaptionPos = mCaptions.indexOf(mSelectedCaption);
            mSelectedCaption = curCaption;
            notifyItemChanged(oldSelectedCaptionPos);
            notifyItemChanged(position);
        });

        if (curCaption.equals(mSelectedCaption)) {
            holder.mFitBCaptionCard.setCardElevation(TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, SELECTED_ELEVATION, holder.mFitBCaptionCard.getContext()
                            .getResources().getDisplayMetrics()));
        }
        else {
            holder.mFitBCaptionCard.setCardElevation(TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, RESTING_ELEVATION, holder.mFitBCaptionCard.getContext()
                            .getResources().getDisplayMetrics()));
        }
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

    public void clearCaptions() {
        mCaptions.clear();
        notifyDataSetChanged();
    }
}