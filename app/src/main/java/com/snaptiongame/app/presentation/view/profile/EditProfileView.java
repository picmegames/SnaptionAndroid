package com.snaptiongame.app.presentation.view.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.authentication.AuthenticationManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Tyler Wong
 */

public class EditProfileView extends RelativeLayout {
    @BindView(R.id.profile_image)
    CircleImageView mProfileImage;
    @BindView(R.id.full_name)
    TextView mFullName;
    @BindView(R.id.email)
    TextView mEmail;
    @BindView(R.id.username)
    TextView mUsername;
    @BindView(R.id.text_layout)
    TextInputLayout mInputLayout;
    @BindView(R.id.name_input)
    TextInputEditText mEditText;
    @BindView(R.id.name_count)
    TextView mNameCount;

    private Context mContext;
    private AuthenticationManager mAuthManager;

    public EditProfileView(Context context, AuthenticationManager authenticationManager) {
        super(context, null);
        mContext = context;
        mAuthManager = authenticationManager;
        init();
    }

    public EditProfileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public EditProfileView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    private void init() {
        View view = inflate(mContext, R.layout.edit_profile, this);
        ButterKnife.bind(this, view);

        Glide.with(mContext)
                .load(mAuthManager.getProfileImageUrl())
                .placeholder(new ColorDrawable(ContextCompat.getColor(mContext, R.color.grey_300)))
                .dontAnimate()
                .into(mProfileImage);

        mFullName.setText(mAuthManager.getUserFullName());
        mEmail.setText(mAuthManager.getEmail());
        mUsername.setText(mAuthManager.getUsername());

        mProfileImage.setOnClickListener(imageView -> {
            Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
            imagePickerIntent.setType("image/*");
            ((AppCompatActivity) mContext).startActivityForResult(imagePickerIntent, 1);
        });
    }

    public void updateProfilePicture(String profileUrl) {
        Glide.with(mContext)
                .load(profileUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .priority(Priority.IMMEDIATE)
                .into(mProfileImage);
    }

    public void updateUsername(String username) {
        mUsername.setText(username);
        mEditText.setText("");
    }

    @OnTextChanged(R.id.name_input)
    public void textChanged(CharSequence sequence) {
        mNameCount.setText(
                String.format(mContext.getString(R.string.count_format), sequence.length()));
    }

    public String getNewUsername() {
        return mEditText.getText().toString();
    }
}
