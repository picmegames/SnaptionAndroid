package com.snaptiongame.snaptionapp.presentation.view.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.snaptiongame.snaptionapp.R;

import butterknife.ButterKnife;

/**
 * @author Tyler Wong
 */

public class NavigationTagSearchBar extends LinearLayout {

    private Context mContext;

    public NavigationTagSearchBar(Context context) {
        super(context);
        mContext = context;
        bindViews();
    }

    public NavigationTagSearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        bindViews();
    }

    public NavigationTagSearchBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        bindViews();
    }

    private void bindViews() {
        View view = inflate(mContext, R.layout.navigation_tag_search_bar, this);
        ButterKnife.bind(this, view);
    }
}
