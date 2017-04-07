package com.snaptiongame.app.presentation.view.game;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.snaptiongame.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nickromero on 2/8/17.
 */

public class CaptionSetViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.set_name)
    TextView mSetName;
    @BindView(R.id.set_image)
    ImageView mSetImage;
    

    public Context mContext;

    public CaptionSetViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }
}
