package com.snaptiongame.snaptionapp.presentation.view.game;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.models.CaptionSet;
import com.snaptiongame.snaptionapp.presentation.view.game.CaptionContract;

import java.util.List;

/**
 * Created by nickromero on 2/8/17.
 */

public class CaptionSetAdapter extends RecyclerView.Adapter{
    public static final float NON_ACTIVE_SET_FADE = .25f;
    private List<CaptionSet> mSets;
    private static CaptionContract.CaptionSetClickListener mCaptionSetClickListener;

    public CaptionSetAdapter(List<CaptionSet> sets, CaptionContract.CaptionSetClickListener captionSetClickListener) {
        mSets = sets;
        mCaptionSetClickListener = captionSetClickListener;
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
        if (!curSet.isCaptionSetActive)
            holder.itemView.setAlpha(NON_ACTIVE_SET_FADE);

        setViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCaptionSetClickListener.captionSetClicked(v, setViewHolder.getAdapterPosition());
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setCaptionSets(List<CaptionSet> captionSets) {
        mSets.clear();
        mSets = captionSets;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return mSets.size();
    }


}
