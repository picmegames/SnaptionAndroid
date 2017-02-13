package com.snaptiongame.snaptionapp.presentation.view.game;

import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.models.CaptionSet;
import com.snaptiongame.snaptionapp.data.models.FitBCaption;
import com.snaptiongame.snaptionapp.presentation.BasePresenter;
import com.snaptiongame.snaptionapp.presentation.BaseView;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class GameContract {
   interface View extends BaseView<Presenter> {
      void showCaptions(List<Caption> captions);
   }

   interface Presenter extends BasePresenter {
      void loadCaptions();
      void loadCaptionSets();
      void loadFitBCaptions();
      void addCaption(String caption, int userId, int fitBId);
   }

   interface CaptionDialogView extends BaseView<Presenter> {
      void showFitBCaptions(List<FitBCaption> captions);
      void showCaptionSets(List<CaptionSet> captionSets);
      void addCaption(Caption caption);
   }




}
