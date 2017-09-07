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
 * @author Nick Romero
 */

/**
 * A FITBCaptionAdapter is used by the the FITB Dialog View to display sets of empty captions.
 */
public class FITBCaptionAdapter extends RecyclerView.Adapter {
    private List<FitBCaption> captions;
    private CaptionContract.CaptionClickListener captionClickListener;
    private FitBCaption selectedCaption;

    private static final int RESTING_ELEVATION = 2;
    private static final int SELECTED_ELEVATION = 8;

    public FITBCaptionAdapter(List<FitBCaption> captions, CaptionContract.CaptionClickListener captionClickListener) {
        this.captions = captions;
        this.captionClickListener = captionClickListener;
    }

    public void setCaptions(List<FitBCaption> captions) {
        this.captions = captions;
        notifyDataSetChanged();
    }

    public void resetCaption() {
        notifyItemChanged(captions.indexOf(selectedCaption));
        selectedCaption = null;
    }

    public FitBCaption getCaption(int index) {
        return captions.get(index);
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
        FitBCaption curCaption = captions.get(position);

        String fitbText = curCaption.getBeforeBlank() + FitBCaption.PLACEHOLDER + curCaption.getAfterBlank();
        ArrayList<String> fitbs = new ArrayList<>(
                Arrays.asList(curCaption.getBeforeBlank(),
                        FitBCaption.PLACEHOLDER,
                        curCaption.getAfterBlank())
        );

        holder.captionTemplateTextView.setText(fitbText);

        holder.currentFitB.setText((position + 1) + "/" + captions.size());

        holder.fitBCaptionCard.setOnClickListener(v -> {
            captionClickListener.captionClicked(v, holder.getAdapterPosition(), fitbs);
            int oldSelectedCaptionPos = captions.indexOf(selectedCaption);
            selectedCaption = curCaption;
            notifyItemChanged(oldSelectedCaptionPos);
            notifyItemChanged(position);
        });

        if (curCaption.equals(selectedCaption)) {
            holder.fitBCaptionCard.setCardElevation(TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, SELECTED_ELEVATION, holder.fitBCaptionCard.getContext()
                            .getResources().getDisplayMetrics()));
        }
        else {
            holder.fitBCaptionCard.setCardElevation(TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, RESTING_ELEVATION, holder.fitBCaptionCard.getContext()
                            .getResources().getDisplayMetrics()));
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return captions.size();
    }

    public void clearCaptions() {
        captions.clear();
        notifyDataSetChanged();
    }
}