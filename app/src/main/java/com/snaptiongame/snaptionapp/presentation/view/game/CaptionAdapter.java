package com.snaptiongame.snaptionapp.presentation.view.game;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.models.Caption;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class CaptionAdapter extends RecyclerView.Adapter {
   private Context mContext;
   private List<Caption> mCaptions;

   public CaptionAdapter(Context context, List<Caption> captions) {
      this.mContext = context;
      this.mCaptions = captions;
   }

   @Override
   public CaptionCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(mContext)
            .inflate(R.layout.caption_card, parent, false);
      return new CaptionCardViewHolder(mContext, view);
   }

   @Override
   public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
      CaptionCardViewHolder holder = (CaptionCardViewHolder) viewHolder;
      Caption curCaption = mCaptions.get(position);

      Glide.with(mContext)
            .load("http://s3.amazonaws.com/37assets/svn/765-default-avatar.png")
            .into(holder.mUserImage);
      holder.mCaption.setText(curCaption.meta.caption);
      holder.mName.setText(curCaption.meta.username);
   }

   public void setCaptions(List<Caption> captions) {
      this.mCaptions = captions;
      notifyDataSetChanged();
   }

   @Override
   public int getItemCount() {
      return mCaptions.size();
   }

   @Override
   public void onAttachedToRecyclerView(RecyclerView recyclerView) {
      super.onAttachedToRecyclerView(recyclerView);
   }
}
