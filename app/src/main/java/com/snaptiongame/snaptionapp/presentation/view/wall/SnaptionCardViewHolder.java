package com.snaptiongame.snaptionapp.presentation.view.wall;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.authentication.AuthenticationManager;
import com.snaptiongame.snaptionapp.data.models.Like;
import com.snaptiongame.snaptionapp.data.providers.SnaptionProvider;
import com.snaptiongame.snaptionapp.presentation.view.game.GameActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class SnaptionCardViewHolder extends RecyclerView.ViewHolder {
   @BindView(R.id.image)
   ImageView mImage;
   @BindView(R.id.top_caption)
   TextView mTopCaption;
   @BindView(R.id.captioner_image)
   CircleImageView mCaptionerImage;
   @BindView(R.id.upvote)
   ImageView mUpvoteButton;
   @BindView(R.id.game_status)
   TextView mGameStatus;

   private Context mContext;
   private AuthenticationManager mAuthManager;

   public int mGameId;
   public String mImageUrl;
   public boolean isUpvoted = false;

   public SnaptionCardViewHolder(Context context, View itemView) {
      super(itemView);
      mContext = context;
      ButterKnife.bind(this, itemView);

      mAuthManager = AuthenticationManager.getInstance(mContext);

      if (Build.VERSION.SDK_INT >= 21) {
         mImage.setClipToOutline(true);
      }

      mUpvoteButton.setOnClickListener(view -> {
         if (isUpvoted) {
            mUpvoteButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_border_grey_400_24dp));
            isUpvoted = false;
         }
         else {
            mUpvoteButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_red_400_24dp));
            isUpvoted = true;
            Toast.makeText(mContext, "Upvoted!", Toast.LENGTH_SHORT).show();
         }
         upvoteSnaption(mGameId, mAuthManager.getSnaptionUserId(), isUpvoted);
      });

      itemView.setOnLongClickListener(view -> {
         PopupMenu menu = new PopupMenu(mContext, itemView);
         menu.getMenuInflater().inflate(R.menu.snaption_card_menu, menu.getMenu());

         menu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
               case R.id.flag:
                  Toast.makeText(mContext, "This will flag a photo as inappropriate!",
                        Toast.LENGTH_LONG).show();
               case R.id.create_game:
                  Toast.makeText(mContext, "This will make a game with the picture.",
                        Toast.LENGTH_LONG).show();
                  break;
               case R.id.share:
                  shareToFacebook();
                  break;
               default:
                  break;
            }
            return true;
         });

         menu.show();
         return true;
      });

      itemView.setOnClickListener(view -> {
         mImage.buildDrawingCache();
         Context cardContext = view.getContext();
         Intent gameIntent = new Intent(cardContext, GameActivity.class);
         gameIntent.putExtra("gameId", mGameId);
         gameIntent.putExtra("image", mImageUrl);

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String transitionName = mContext.getString(R.string.shared_transition);
            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                  (Activity) mContext, mImage, transitionName);
            cardContext.startActivity(gameIntent, transitionActivityOptions.toBundle());
         }
         else {
            cardContext.startActivity(gameIntent);
         }
      });
   }

   private void upvoteSnaption(int gameId, int userId, boolean isUpvoted) {
      SnaptionProvider.upvoteSnaption(gameId, new Like(userId, isUpvoted))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(like -> {
            }, Timber::e, () -> Timber.i("Successfully liked snaption!"));
   }

   private void shareToFacebook() {
      mImage.setDrawingCacheEnabled(true);
      SharePhoto photo = new SharePhoto.Builder()
            .setBitmap(mImage.getDrawingCache())
            .build();

      SharePhotoContent content = new SharePhotoContent.Builder()
            .addPhoto(photo)
            .build();

      ShareDialog shareDialog = new ShareDialog((AppCompatActivity) mContext);
      shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
   }
}
