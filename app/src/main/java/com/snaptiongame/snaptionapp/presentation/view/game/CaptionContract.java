package com.snaptiongame.snaptionapp.presentation.view.game;

import android.view.View;

/**
 * Created by nickromero on 2/12/17.
 */

public class CaptionContract {
    public interface CaptionSetClickListener {
        void captionSetClicked(View v, int position);
    }

    public interface CaptionClickListener {
        void captionClicked(View v, int position, FITBCaptionCardViewHolder holder);
    }
}
