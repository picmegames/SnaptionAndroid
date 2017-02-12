package com.snaptiongame.snaptionapp.presentation.view.game;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.models.CaptionSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickromero on 2/8/17.
 */

public class CaptionSetAdapter extends RecyclerView.Adapter{
    private List<CaptionSet> mSets;

    public CaptionSetAdapter(List<CaptionSet> sets) {
        mSets = sets;
        initData();
    }


    @Override
    public CaptionSetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.caption_set, parent, false);

        return new CaptionSetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CaptionSetViewHolder setViewHolder = (CaptionSetViewHolder) holder;
        CaptionSet curSet = mSets.get(position);

        setViewHolder.mSetName.setText(curSet.getSetName());
        setViewHolder.mSetImage.setImageResource(R.drawable.snaption_logo);
        setViewHolder.sSetCount.setText(mSets.get(position).getCaptionsUnlocked() + "/" +
                mSets.get(position).getTotalCaptions());

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mSets.size();
    }

    private void initData() {
        ArrayList<Caption> newCaptions = new ArrayList<Caption>();
        newCaptions.add(new Caption(0, "Hello", 1));
        newCaptions.add(new Caption(1, "HelloHello", 1));
        newCaptions.add(new Caption(2, "HelloHellllllooo", 1));

        CaptionSet set = new CaptionSet(newCaptions);
        set.setCaptionsUnlocked(7);
        set.setSetName("Halloween");

        mSets.add(set);
        set = new CaptionSet(newCaptions);
        set.setCaptionsUnlocked(1);
        set.setSetName("Cal Poly");

        mSets.add(set);
        set = new CaptionSet(newCaptions);
        set.setCaptionsUnlocked(5);
        set.setSetName("2016");
        mSets.add(set);

    }
}