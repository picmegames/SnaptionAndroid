package com.snaptiongame.app.presentation.view.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.snaptiongame.app.R;

/**
 * @author Tyler Wong
 */

@SuppressLint("AppCompatCustomView")
public class DynamicImageView extends ImageView {
    private float mAspectRatio;
    private int mMaxHeight;

    public DynamicImageView(Context context) {
        super(context);
    }

    public DynamicImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMaxHeight = (int) context.getResources().getDimension(R.dimen.max_image_height);
    }

    public DynamicImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAspectRatio(float aspectRatio) {
        mAspectRatio = aspectRatio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (width / mAspectRatio);

        if (height > mMaxHeight) {
            height = mMaxHeight;
        }

        setMeasuredDimension(width, height);
    }
}
