package com.snaptiongame.app.presentation.view.game;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.CaptionSet;

import java.util.List;

/**
 * @author Nick Romero
 */

public class CaptionSetAdapter extends RecyclerView.Adapter {

    private List<CaptionSet> sets;
    private CaptionContract.CaptionSetClickListener captionSetClickListener;

    private static final float NO_ALPHA = 1.0f;
    private static final float NON_ACTIVE_SET_FADE = .25f;

    public CaptionSetAdapter(List<CaptionSet> sets, CaptionContract.CaptionSetClickListener captionSetClickListener) {
        this.sets = sets;
        this.captionSetClickListener = captionSetClickListener;
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
        CaptionSet curSet = sets.get(position);
        setViewHolder.setName.setText(curSet.getSetName());
        setViewHolder.setImage.setImageResource(R.drawable.snaption_logo);

        if (!curSet.isCaptionSetActive()) {
            holder.itemView.setAlpha(NON_ACTIVE_SET_FADE);
        }
        else {
            holder.itemView.setAlpha(NO_ALPHA);
        }

        setViewHolder.itemView.setOnClickListener(v ->
                captionSetClickListener.captionSetClicked(v, curSet.getId(), position));
    }

    public String getSetName(int position) {
        return sets.get(position).getSetName();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setCaptionSets(List<CaptionSet> captionSets) {
        sets = captionSets;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }
}
