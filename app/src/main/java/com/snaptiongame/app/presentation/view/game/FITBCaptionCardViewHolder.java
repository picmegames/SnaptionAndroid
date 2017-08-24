package com.snaptiongame.app.presentation.view.game;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.snaptiongame.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Nick Romero
 */
public class FITBCaptionCardViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.fitb_caption_card)
    CardView fitBCaptionCard;
    @BindView(R.id.fitb_caption_card_text)
    TextView captionTemplateTextView;
    @BindView(R.id.cur_fitb)
    TextView currentFitB;

    public String captionTemplate;
    public String curFitB;

    public FITBCaptionCardViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        captionTemplate = captionTemplateTextView.getText().toString();
        curFitB = currentFitB.getText().toString();

        view.setOnClickListener(v -> {

        });
    }
}