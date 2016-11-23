package com.snaptiongame.snaptionapp.presentation.view.wall;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.models.Snaption;

/**
 * @author Tyler Wong
 */

public class WallAdapter extends RecyclerView.Adapter {
   private Context mContext;
   private Snaption[] mSnaptions;

   public WallAdapter(Context context, Snaption[] snaptions) {
      this.mContext = context;
      this.mSnaptions = snaptions;
   }

   @Override
   public SnaptionCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(mContext)
            .inflate(R.layout.snaption_card, parent, false);
      return new SnaptionCardViewHolder(view);
   }

   @Override
   public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
      SnaptionCardViewHolder holder = (SnaptionCardViewHolder) viewHolder;
      Snaption curSnaption = mSnaptions[position];

   }

   @Override
   public int getItemCount() {
      return mSnaptions.length;
   }

   @Override
   public void onAttachedToRecyclerView(RecyclerView recyclerView) {
      super.onAttachedToRecyclerView(recyclerView);
   }
}
