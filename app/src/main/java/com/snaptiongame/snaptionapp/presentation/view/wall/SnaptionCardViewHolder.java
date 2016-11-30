package com.snaptiongame.snaptionapp.presentation.view.wall;

import android.content.Context;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Tyler Wong
 */

public class SnaptionCardViewHolder extends RecyclerView.ViewHolder {
   @BindView(R.id.image)
   ImageView mImage;
   @BindView(R.id.commenter_image)
   CircleImageView mCommenterImage;
   @BindView(R.id.commenter_name)
   TextView mCommenterName;
   @BindView(R.id.picker_image)
   CircleImageView mPickerImage;
   @BindView(R.id.picker_name)
   TextView mPickerName;
   @BindView(R.id.top_caption)
   TextView mTopCaption;
   @BindView(R.id.menu_button)
   ImageButton mMenuButton;

   private Context mContext;

   public SnaptionCardViewHolder(Context context, View itemView) {
      super(itemView);
      mContext = context;
      ButterKnife.bind(this, itemView);

      mMenuButton.setOnClickListener(view -> {
         PopupMenu menu = new PopupMenu(mContext, mMenuButton);
         menu.getMenuInflater().inflate(R.menu.snaption_card_menu, menu.getMenu());

         menu.setOnMenuItemClickListener(item -> {
            switch(item.getItemId()) {
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
