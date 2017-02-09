package com.snaptiongame.snaptionapp.presentation.view.wall;

import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.presentation.BasePresenter;
import com.snaptiongame.snaptionapp.presentation.BaseView;

import java.util.List;

/**
 * This class serves as a contract for the Wall Fragment
 * and the Wall Presenter.
 *
 * @author Tyler Wong
 * @version 1.0
 */
public class WallContract {

   /**
    * This is a template for a view.
    */
   interface View extends BaseView<Presenter> {
      void showGames(List<Snaption> snaptions);
   }

   /**
    * This is a template for a presenter.
    */
   interface Presenter extends BasePresenter {
      void loadGames();
   }
}
