package com.snaptiongame.app.presentation.view.activityfeed;

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
 * @author Tyler Wong
 */

public class ActivityFeedItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.user_image)
    CircleImageView mUserImage;
    @BindView(R.id.activity_message)
    TextView mActivityMessage;
    @BindView(R.id.content_image)
    ImageView mContentImage;

    public Context mContext;
    public String mImageUrl;
    public int mActivityType;

    public ActivityFeedItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
    }
}
