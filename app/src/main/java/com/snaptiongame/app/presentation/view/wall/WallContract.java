package com.snaptiongame.app.presentation.view.wall;

import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.presentation.BasePresenter;
import com.snaptiongame.app.presentation.BaseView;

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
        void showGames(List<Game> games);
    }

    /**
     * This is a template for a presenter.
     */
    interface Presenter extends BasePresenter {
        void loadGames();
    }
}
