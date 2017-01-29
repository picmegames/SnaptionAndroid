package com.snaptiongame.snaptionapp.presentation.view.profile;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.authentication.AuthenticationManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Tyler Wong
 */

public class ProfileInfoView extends NestedScrollView {
   @BindView(R.id.email)
   TextView mEmail;

   private Context mContext;

   private AuthenticationManager mAuthManager;

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
      mAuthManager = AuthenticationManager.getInstance(mContext);

      mEmail.setText(mAuthManager.getEmail());
   }
}
