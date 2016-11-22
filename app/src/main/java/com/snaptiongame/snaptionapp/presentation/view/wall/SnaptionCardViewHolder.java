package com.snaptiongame.snaptionapp.presentation.view.wall;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.snaptiongame.snaptionapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Tyler Wong
 */

public class SnaptionCardViewHolder extends RecyclerView.ViewHolder {
   @BindView(R.id.image)
   ImageView mImage;
   @BindView(R.id.title)
   TextView mTitle;

   public SnaptionCardViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
   }
}
