package com.snaptiongame.snaptionapp.presentation.view.wall;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.models.Snaption;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class WallAdapter extends RecyclerView.Adapter {
   private List<Snaption> mSnaptions;

   public WallAdapter(List<Snaption> snaptions) {
      this.mSnaptions = snaptions;
   }

   @Override
   public SnaptionCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.snaption_card, parent, false);
      return new SnaptionCardViewHolder(view);
   }

   @Override
   public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
      SnaptionCardViewHolder holder = (SnaptionCardViewHolder) viewHolder;
      Snaption curSnaption = mSnaptions.get(position);

      holder.mGameId = curSnaption.id;

      if (curSnaption.picture != null) {
         holder.mImage.layout(0, 0, 0, 0);
         Glide.with(holder.mContext)
               .load(curSnaption.picture)
               .into(holder.mImage);
         holder.mImageUrl = curSnaption.picture;
      }
      else {
         Glide.clear(holder.mImage);
      }

      if (curSnaption.topCaption != null) {
         if (curSnaption.topCaption.creatorPicture != null) {
            Glide.with(holder.mContext)
                  .load(curSnaption.topCaption.creatorPicture)
                  .into(holder.mCaptionerImage);
         }
         else {
            holder.mCaptionerImage.setImageDrawable(TextDrawable.builder()
                  .beginConfig()
                  .width(40)
                  .height(40)
                  .toUpperCase()
                  .endConfig()
                  .buildRound(curSnaption.topCaption.creatorName.substring(0, 1),
                        ColorGenerator.MATERIAL.getRandomColor()));
         }
         holder.mTopCaption.setText(curSnaption.topCaption.assocFitB.beforeBlank
               + curSnaption.topCaption.caption + curSnaption.topCaption.assocFitB.afterBlank);
      }
      else {
         Glide.with(holder.mContext)
               .load(R.mipmap.ic_launcher)
               .into(holder.mCaptionerImage);
         holder.mTopCaption.setText(holder.mContext.getString(R.string.default_caption));
      }

      if (curSnaption.endDate != 0) {
         holder.mGameStatus.setText(holder.mContext.getString(R.string.game_closed));
      }
      else {
         holder.mGameStatus.setText(holder.mContext.getString(R.string.game_open));
      }
   }

   public void clearSnaptions() {
      mSnaptions.clear();
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
