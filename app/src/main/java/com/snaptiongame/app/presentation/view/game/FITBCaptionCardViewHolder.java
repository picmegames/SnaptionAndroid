package com.snaptiongame.app.presentation.view.game;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snaptiongame.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nickromero on 2/7/17.
 */
public class FITBCaptionCardViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.fitb_caption_card)
    CardView mFitBCaptionCard;
    @BindView(R.id.fitb_caption_card_text)
    TextView mCaptionTemplateTextView;
    @BindView(R.id.cur_fitb)
    TextView mCurrentFitB;
    @BindView(R.id.relative_layout_fitb)
    RelativeLayout relativeLayout;

    public String mCaptionTemplate;
    public String mCurFitB;

    public FITBCaptionCardViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        mCaptionTemplate = mCaptionTemplateTextView.getText().toString();
        mCurFitB = mCurrentFitB.getText().toString();
        mFitBCaptionCard.findViewById(R.id.fitb_caption_card);
    }
}