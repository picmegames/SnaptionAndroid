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
 * @author Nick Romero
 */

public class CaptionSetViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.set_name)
    TextView setName;
    @BindView(R.id.set_image)
    ImageView setImage;

    public Context context;

    public CaptionSetViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }
}
