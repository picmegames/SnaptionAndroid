package com.snaptiongame.snaptionapp.presentation.view.wall;

import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.presentation.BasePresenter;
import com.snaptiongame.snaptionapp.presentation.BaseView;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class WallContract {
   interface View extends BaseView<Presenter> {
      void showGames(List<Snaption> snaptions);
   }

   interface Presenter extends BasePresenter {
      void loadGames();
   }
}
