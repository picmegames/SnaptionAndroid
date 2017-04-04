package com.snaptiongame.app.presentation.view.photo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.presentation.view.transitions.TransitionUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Tyler Wong
 */

public class ImmersiveActivity extends AppCompatActivity {
    @BindView(R.id.photo_view)
    PhotoView mPhotoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immersive);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(Game.IMAGE_URL);
        ViewCompat.setTransitionName(mPhotoView, imageUrl);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            supportPostponeEnterTransition();
            Glide.with(this)
                    .load(imageUrl)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .priority(Priority.IMMEDIATE)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            supportStartPostponedEnterTransition();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            supportStartPostponedEnterTransition();
                            return false;
                        }
                    })
                    .into(mPhotoView);
        }

        TransitionUtils.setupArcTransition(this, getWindow());
    }
}
