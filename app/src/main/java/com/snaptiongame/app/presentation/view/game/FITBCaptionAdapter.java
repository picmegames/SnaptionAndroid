package com.snaptiongame.app.presentation.view.game;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.FitBCaption;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickromero on 2/7/17.
 */

public class FITBCaptionAdapter extends BaseAdapter {
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
    public int getCount() {
        return mCaptions.size();
    }

    @Override
    public Object getItem(int position) {
        return mCaptions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = mInflater.inflate(R.layout.fitb_caption_card, parent, false);

        view.setSelected(true);
        FitBCaption curCaption = mCaptions.get(position);


        TextView fitbText = (TextView) view.findViewById(R.id.fitb_caption_card_text);
        fitbText.setText(curCaption.beforeBlank + curCaption.placeholderText + curCaption.afterBlank);
        TextView curFITB = (TextView) view.findViewById(R.id.cur_fitb);
        curFITB.setText((position + 1) + "/" + mCaptions.size());

        view.setTag(position);
        
        view.findViewById(R.id.fitb_caption_card).setOnClickListener(v -> {
            mCaptionClickListener.captionClicked(v, position);

            if (!areCardsChecked.get(position)) {
                mOriginalBackground = v.getBackground();
                v.setBackgroundResource(R.drawable.card_border_color_pink);
                areCardsChecked.set(position, true);
            }
            else {
                v.setBackground(mOriginalBackground);
                areCardsChecked.set(position, false);
            }
        });
        return view;
    }
}
