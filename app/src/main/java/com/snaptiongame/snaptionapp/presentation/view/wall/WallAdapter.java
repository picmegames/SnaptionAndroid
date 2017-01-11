package com.snaptiongame.snaptionapp.presentation.view.wall;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.models.Snaption;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class WallAdapter extends RecyclerView.Adapter {
   private Context mContext;
   private List<Snaption> mSnaptions;

   public WallAdapter(Context context, List<Snaption> snaptions) {
      this.mContext = context;
      this.mSnaptions = snaptions;
   }

   @Override
   public SnaptionCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(mContext)
            .inflate(R.layout.snaption_card, parent, false);
      return new SnaptionCardViewHolder(mContext, view);
   }

   @Override
   public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
      SnaptionCardViewHolder holder = (SnaptionCardViewHolder) viewHolder;
      Snaption curSnaption = mSnaptions.get(position);

      holder.mGameId = curSnaption.id;

      if (curSnaption.meta.picture != null) {
         Glide.with(mContext)
               .load(curSnaption.meta.picture)
               .into(holder.mImage);
         holder.mImageUrl = curSnaption.meta.picture;
      }
      else {
         Glide.clear(holder.mImage);
      }

      Glide.with(mContext)
            .load("http://s3.amazonaws.com/37assets/svn/765-default-avatar.png")
            .into(holder.mCaptionerImage);

      holder.mTopCaption.setText("It's working!");
   }

   public void setSnaptions(List<Snaption> snaptions) {
      this.mSnaptions = snaptions;
      notifyDataSetChanged();
   }

   @Override
   public int getItemCount() {
      return mSnaptions.size();
   }

   @Override
   public void onAttachedToRecyclerView(RecyclerView recyclerView) {
      super.onAttachedToRecyclerView(recyclerView);
   }
}
