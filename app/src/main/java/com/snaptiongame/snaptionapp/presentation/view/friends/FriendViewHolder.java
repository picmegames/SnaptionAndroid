package com.snaptiongame.snaptionapp.presentation.view.friends;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.snaptiongame.snaptionapp.R;

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
    TextView mUserName;

    private Context mContext;

    public FriendViewHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
        ButterKnife.bind(this, itemView);
    }
}
