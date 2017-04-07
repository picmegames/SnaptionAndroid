package com.snaptiongame.app.presentation.view.game;

import android.view.View;

import java.util.List;

/**
 * Created by nickromero on 2/12/17.
 */

public class CaptionContract {
    public interface CaptionSetClickListener {
        void captionSetClicked(View v, int position);
    }

    public interface CaptionClickListener {
        void captionClicked(View v, int position, List<String> fitbs);
    }
}
