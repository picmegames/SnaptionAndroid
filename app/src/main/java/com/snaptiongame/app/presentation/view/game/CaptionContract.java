package com.snaptiongame.app.presentation.view.game;

import android.view.View;

import java.util.List;

/**
 * @author Nick Romero
 */

public class CaptionContract {
    public interface CaptionSetClickListener {
        void captionSetClicked(View v, int setId, int position);
    }

    public interface CaptionClickListener {
        void captionClicked(View v, int position, List<String> fitbs);
    }
}
