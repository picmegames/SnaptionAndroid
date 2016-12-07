package com.snaptiongame.snaptionapp.presentation.view.game;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.snaptiongame.snaptionapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Tyler Wong
 */

public class CaptionCardViewHolder extends RecyclerView.ViewHolder {
   @BindView(R.id.user_image)
   CircleImageView mUserImage;
   @BindView(R.id.name)
   TextView mName;
   @BindView(R.id.caption)
   TextView mCaption;

   private Context mContext;

   public CaptionCardViewHolder(Context context, View itemView) {
      super(itemView);
      this.mContext = context;
      ButterKnife.bind(this, itemView);
   }
}
