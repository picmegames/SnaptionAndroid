package com.snaptiongame.snaptionapp.presentation.view.profile;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

import com.snaptiongame.snaptionapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Tyler Wong
 */

public class ProfileInfoView extends NestedScrollView {
   @BindView(R.id.tab_layout)
   TabLayout mTabLayout;
   @BindView(R.id.view_pager)
   ViewPager mViewPager;

   private Context mContext;
   private ProfileContract.Presenter mPresenter;

   public ProfileInfoView(Context context) {
      super(context, null);
      mContext = context;
      init();
   }

   public ProfileInfoView(Context context, AttributeSet attrs) {
      super(context, attrs);
      mContext = context;
      init();
   }

   public ProfileInfoView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      mContext = context;
      init();
   }

   private void init() {
      View view = inflate(mContext, R.layout.profile_info, this);
      ButterKnife.bind(this, view);

      mViewPager.setAdapter(
            new ProfileInfoPageAdapter(((ProfileActivity) mContext).getSupportFragmentManager(), mContext));

      mTabLayout.setupWithViewPager(mViewPager);
      int white = ContextCompat.getColor(mContext, android.R.color.white);
      mTabLayout.setTabTextColors(white, white);
   }

   public void setPresenter(ProfileContract.Presenter presenter) {
      mPresenter = presenter;
   }
}
