package com.snaptiongame.snaptionapp.presentation.view.game;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snaptiongame.snaptionapp.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nickromero on 2/7/17.
 */
public class FITBCaptionCardViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fitb_caption_card_text)
    public TextView mCaptionTemplateTextView;
    @BindView(R.id.cur_fitb)
    public TextView mCurrentFitB;

    public String mCaptionTemplate;


    public String mCurFitB;


    public FITBCaptionCardViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);


    }


}
