package com.snaptiongame.snaptionapp.presentation.view.game;

import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.presentation.BasePresenter;
import com.snaptiongame.snaptionapp.presentation.BaseView;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class GameContract {
   interface View extends BaseView<Presenter> {
      void showCaptions(List<Caption> captions);
      void addCaption(Caption caption);
   }

   interface Presenter extends BasePresenter {
      void loadCaptions();
      void addCaption(String caption);
   }
}
