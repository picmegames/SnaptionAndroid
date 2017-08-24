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
import com.bumptech.glide.request.RequestOptions;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.auth.AuthManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Tyler Wong
 */

public class EditProfileView extends RelativeLayout {
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.full_name)
    TextView fullName;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.text_layout)
    TextInputLayout inputLayout;
    @BindView(R.id.name_input)
    TextInputEditText editText;
    @BindView(R.id.name_count)
    TextView nameCount;

    private Context context;

    public EditProfileView(Context context) {
        super(context, null);
        this.context = context;
        init();
    }

    public EditProfileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public EditProfileView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    private void init() {
        View view = inflate(context, R.layout.edit_profile, this);
        ButterKnife.bind(this, view);

        RequestOptions options = new RequestOptions()
                .placeholder(new ColorDrawable(ContextCompat.getColor(context, R.color.grey_300)))
                .dontAnimate();

        Glide.with(context)
                .load(AuthManager.getProfileImageUrl())
                .apply(options)
                .into(profileImage);

        fullName.setText(AuthManager.getUserFullName());
        email.setText(AuthManager.getEmail());
        username.setText(AuthManager.getUsername());

        profileImage.setOnClickListener(imageView -> {
            Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
            imagePickerIntent.setType("image/*");
            ((AppCompatActivity) context).startActivityForResult(imagePickerIntent, ProfileActivity.IMAGE_PICKER_RESULT);
        });
    }

    public void updateProfilePicture(String profileUrl) {
        RequestOptions options = new RequestOptions()
                .priority(Priority.IMMEDIATE);

        Glide.with(context)
                .load(profileUrl)
                .apply(options)
                .into(profileImage);
    }

    public void updateUsername(String username) {
        this.username.setText(username);
    }

    @OnTextChanged(R.id.name_input)
    public void textChanged(CharSequence sequence) {
        nameCount.setText(
                String.format(context.getString(R.string.count_format), sequence.length()));
    }

    public void clearUsernameField() {
        editText.setText("");
    }

    public String getNewUsername() {
        return editText.getText().toString();
    }
}
