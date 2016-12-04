package com.snaptiongame.snaptionapp.presentation.view.wall;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.presentation.view.MainActivity;
import com.snaptiongame.snaptionapp.presentation.view.game.GameActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Tyler Wong
 */

public class SnaptionCardViewHolder extends RecyclerView.ViewHolder {
   @BindView(R.id.image)
   ImageView mImage;
   @BindView(R.id.captioner_image)
   CircleImageView mCaptionerImage;
   @BindView(R.id.captioner_name)
   TextView mCaptionerName;
   @BindView(R.id.picker_image)
   CircleImageView mPickerImage;
   @BindView(R.id.picker_name)
   TextView mPickerName;
   @BindView(R.id.top_caption)
   TextView mTopCaption;
   @BindView(R.id.menu_button)
   ImageButton mMenuButton;

   private Context mContext;
   public String mImageUrl;

   public SnaptionCardViewHolder(Context context, View itemView) {
      super(itemView);
      mContext = context;
      ButterKnife.bind(this, itemView);

      mMenuButton.setOnClickListener(view -> {
         PopupMenu menu = new PopupMenu(mContext, mMenuButton);
         menu.getMenuInflater().inflate(R.menu.snaption_card_menu, menu.getMenu());

         menu.setOnMenuItemClickListener(item -> {
            switch(item.getItemId()) {
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
      });

      itemView.setOnClickListener(view -> {
         mImage.buildDrawingCache();
         Context cardContext = view.getContext();
         Intent gameIntent = new Intent(cardContext, GameActivity.class);
         gameIntent.putExtra("image", mImageUrl);

         if (Build.VERSION.SDK_INT >= 21) {
            String transitionName = mContext.getString(R.string.shared_transition);
            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                  (MainActivity) mContext, mImage, transitionName);
            cardContext.startActivity(gameIntent, transitionActivityOptions.toBundle());
         }
         else {
            cardContext.startActivity(gameIntent);
         }
      });
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
