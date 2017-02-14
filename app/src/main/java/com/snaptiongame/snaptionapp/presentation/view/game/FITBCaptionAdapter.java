package com.snaptiongame.snaptionapp.presentation.view.game;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.models.FitBCaption;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickromero on 2/7/17.
 */

public class FITBCaptionAdapter extends RecyclerView.Adapter {
    private List<FitBCaption> mCaptions;
    private static CaptionContract.CaptionClickListener mCaptionClickListener;
    private List<View> fitbViews;

    public FITBCaptionAdapter(List<FitBCaption> captions, CaptionContract.CaptionClickListener captionClickListener) {
        mCaptions = captions;
        mCaptionClickListener = captionClickListener;
        fitbViews = new ArrayList<>();
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


        holder.mCaptionTemplate = curCaption.beforeBlank + curCaption.placeholderText + curCaption.afterBlank;
        holder.mCaptionTemplateTextView.setText(holder.mCaptionTemplate);
        holder.mCurFitB = String.valueOf(position);
        holder.mCurrentFitB.setText((position + 1) + "/" + mCaptions.size());
        holder.itemView.setTag(position);

        holder.itemView.findViewById(R.id.fitb_caption_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCaptionClickListener.captionClicked(v, position, holder);
                v.setBackgroundResource(R.drawable.card_border_color_pink);


            }
        });


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



    /*
     /*
   String[] textPieces = holder.mCaptionTemplateTextView.getText().toString().split(FITB_PLACEHOLDER);

        final String beforeText = textPieces[0];
        String afterText = "";


        String finalAfterText = afterText;
        ((EditText) (fitBEditTextLayout.findViewById(R.id.fitbEditText))).addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {




                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        holder.mCaptionTemplateTextView.setText("");



                        holder.mCaptionTemplateTextView.setText(beforeText + s + finalAfterText);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                }
        );
    */


}
