package com.snaptiongame.app.presentation.view.game;

import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.snaptiongame.app.data.models.Caption;
import com.snaptiongame.app.data.models.CaptionSet;
import com.snaptiongame.app.data.models.FitBCaption;
import com.snaptiongame.app.data.models.Like;
import com.snaptiongame.app.presentation.BasePresenter;
import com.snaptiongame.app.presentation.BaseView;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class GameContract {
    interface View extends BaseView<Presenter> {
        void showCaptions(List<Caption> captions);

        void addCaption(Caption caption);

        void setPickerInfo(String profileUrl, String name);

        void generateInviteUrl(String inviteToken);
    }

    interface Presenter extends BasePresenter {
        void loadCaptions();

        void loadCaptionSets();

        void loadFitBCaptions(int setId);

        void addCaption(int fitBId, String caption);

        void upvoteOrFlagGame(Like request);

        void shareToFacebook(AppCompatActivity activity, ImageView image);

        void getBranchToken(int gameId);
    }

    interface CaptionDialogView extends BaseView<Presenter> {
        void showFitBCaptions(List<FitBCaption> captions);

        void showCaptionSets(List<CaptionSet> captionSets);
    }
}