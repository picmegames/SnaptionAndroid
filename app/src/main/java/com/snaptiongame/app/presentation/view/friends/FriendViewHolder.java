package com.snaptiongame.app.presentation.view.friends;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.snaptiongame.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Brian Gouldsberry
 */

public class FriendViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.primary_text)
    TextView mName;
    @BindView(R.id.image)
    CircleImageView mImage;
    @BindView(R.id.secondary_text)
    TextView mUsernameField;
    @BindView(R.id.add_remove_friend_icon)
    ImageView mAddRemoveFriendIcon;

    public Context mContext;

    public FriendViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }
}
