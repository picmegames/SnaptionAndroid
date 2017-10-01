package com.snaptiongame.app.presentation.view.photo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.snaptiongame.app.R;
import com.snaptiongame.app.presentation.view.utils.TransitionUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Tyler Wong
 */

public class ImmersiveActivity extends AppCompatActivity {
    @BindView(R.id.photo_view)
    PhotoView photoView;

    public static final String IMAGE_URL = "imageUrl";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immersive);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(IMAGE_URL);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .dontAnimate()
                    .priority(Priority.IMMEDIATE);

            Glide.with(this)
                    .load(imageUrl)
                    .apply(options)
                    .into(photoView);
        }
    }

    @OnClick(R.id.photo_view)
    public void closeImmersiveActivity() {
        super.onBackPressed();
    }
}
