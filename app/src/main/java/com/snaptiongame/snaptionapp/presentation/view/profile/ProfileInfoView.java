package com.snaptiongame.snaptionapp.presentation.view.profile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.authentication.AuthenticationManager;
import com.snaptiongame.snaptionapp.data.models.User;
import com.snaptiongame.snaptionapp.data.providers.UserProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class ProfileInfoView extends NestedScrollView {
   @BindView(R.id.email_card)
   CardView mEmailView;
   @BindView(R.id.email)
   TextView mEmail;
   @BindView(R.id.username_card)
   CardView mUsernameView;
   @BindView(R.id.username)
   TextView mUsername;

   private Context mContext;
   private AuthenticationManager mAuthManager;
   private String mOldUsername;

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
      mUsername.setText(mAuthManager.getSnaptionUsername());

      mUsernameView.setOnClickListener(userView -> {
         new MaterialDialog.Builder(mContext)
               .title(R.string.edit_username)
               .inputType(InputType.TYPE_CLASS_TEXT)
               .input("", "", (@NonNull MaterialDialog dialog, CharSequence input) ->
                  updateUsername(view, mAuthManager.getSnaptionUserId(), input.toString())
               )
               .show();
      });
   }

   private void updateUsername(View view, int userId, String name) {
      mOldUsername = mUsername.getText().toString();
      UserProvider.updateUser(userId, new User(name))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<User>() {
               @Override
               public void onCompleted() {
                  Timber.i("Username updated successfully");
                  if (!name.equals(mOldUsername)) {
                     Snackbar
                           .make(view, mContext.getString(R.string.update_success), Snackbar.LENGTH_LONG)
                           .setAction(mContext.getString(R.string.undo), view ->
                                 updateUsername(view, userId, mOldUsername))
                           .show();
                  }
               }

               @Override
               public void onError(Throwable e) {
                  Timber.e(e);
               }

               @Override
               public void onNext(User user) {
                  mUsername.setText(user.username);
                  mAuthManager.saveSnaptionUsername(user.username);
               }
            });
   }
}
