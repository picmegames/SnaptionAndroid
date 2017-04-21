package com.snaptiongame.app.presentation.view.creategame2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.snaptiongame.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * @author Tyler Wong
 */

public class CreateGamePhotoFragment extends Fragment {

    @BindView(R.id.camera_animation)
    LottieAnimationView mAnimationView;
    @BindView(R.id.image)
    ImageView mImage;

    private Uri mUri;
    private Unbinder mUnbinder;

    private static final String INTENT_TYPE = "image/*";

    public static CreateGamePhotoFragment newInstance() {
        return new CreateGamePhotoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.create_game_photo_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mAnimationView.setAnimation(getString(R.string.anim_4));
        mAnimationView.loop(true);
        mAnimationView.playAnimation();

        return view;
    }

    @OnClick(R.id.image)
    public void getImage() {
        if (isStoragePermissionGranted()) {
            pickImage();
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            return true;
        }
    }

    private void pickImage() {
        Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
        imagePickerIntent.setType(INTENT_TYPE);
        startActivityForResult(imagePickerIntent, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mAnimationView.pauseAnimation();
            mAnimationView.setVisibility(View.GONE);
            mUri = data.getData();
            Glide.with(this)
                    .load(mUri)
                    .bitmapTransform(new FitCenter(getContext()))
                    .into(mImage);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
