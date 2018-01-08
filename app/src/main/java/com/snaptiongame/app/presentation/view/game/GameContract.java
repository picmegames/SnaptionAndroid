package com.snaptiongame.app.presentation.view.game;

import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.snaptiongame.app.data.models.Caption;
import com.snaptiongame.app.data.models.CaptionSet;
import com.snaptiongame.app.data.models.FitBCaption;
import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.data.models.GameAction;
import com.snaptiongame.app.data.models.Tag;
import com.snaptiongame.app.presentation.BasePresenter;
import com.snaptiongame.app.presentation.BaseView;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class GameContract {
    interface View extends BaseView<Presenter> {
        void showGame(String image, int gameId, int pickerId, String pickerName, String pickerImage,
                      boolean beenUpvoted, boolean beenFlagged, boolean isClosed, boolean isPublic);

        void showCaptions(List<Caption> captions);

        void generateInviteUrl(String inviteToken);

        void showCaptionSets(List<CaptionSet> captionSets);

        void showFitBCaptions(List<FitBCaption> fitBCaptions);

        void showRandomCaptions(List<FitBCaption> randomCaptions);

        void showPrivateGameDialog(List<Friend> invitedUsers);

        void showTags(List<Tag> tags);

        void onGameUpdated(String type);

        void onGameErrored(String type);

        void setRefreshing(boolean isRefreshing);

        void resetScrollState();
      
        void showCaptionSubmissionError();
    }

    interface Presenter extends BasePresenter {
        void loadCaptions(int page);

        void loadCaptionSets();

        void loadFitBCaptions(int setId);

        void loadAllFITBCaptions();

        void addCaption(int fitBId, String caption);

        void upvoteOrFlagGame(GameAction request);

        void shareToFacebook(AppCompatActivity activity, ImageView image);

        void getBranchToken(int gameId);

        void refreshCaptions();

        void loadInvitedUsers(int gameId);

        void loadTags(int gameId);

        void loadGame(int gameId);

        void setGameId(int gameId);
    }
}
