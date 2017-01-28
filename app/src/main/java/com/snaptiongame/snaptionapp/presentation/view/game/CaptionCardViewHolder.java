package com.snaptiongame.snaptionapp.presentation.view.game;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.authentication.AuthenticationManager;
import com.snaptiongame.snaptionapp.data.models.LikeRequest;
import com.snaptiongame.snaptionapp.data.providers.CaptionProvider;

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
   @BindView(R.id.like)
   ImageView mLike;
   @BindView(R.id.number_of_likes)
   TextView mNumberOfLikes;

   private Context mContext;
   private AuthenticationManager mAuthManager;

   public boolean isLiked = false;
   public int captionId;

   public CaptionCardViewHolder(Context context, View itemView) {
      super(itemView);
      this.mContext = context;
      ButterKnife.bind(this, itemView);

      mAuthManager = AuthenticationManager.getInstance(mContext);

      mLike.setOnClickListener(view -> {
         if (isLiked) {
            mLike.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_border_grey_400_24dp));
            isLiked = false;
            mNumberOfLikes.setText(String.valueOf(Integer.parseInt(mNumberOfLikes.getText().toString()) - 1));
         }
         else {
            mLike.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_red_400_24dp));
            isLiked = true;
            mNumberOfLikes.setText(String.valueOf(Integer.parseInt(mNumberOfLikes.getText().toString()) + 1));
         }
         CaptionProvider.upvoteCaption(captionId, new LikeRequest(mAuthManager.getSnaptionUserId(), isLiked));
      });
   }
}
