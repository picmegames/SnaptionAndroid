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

    public static final int MY_WALL = 0;
    public static final int DISCOVER = 1;
    public static final int POPULAR = 2;
    public static final int HISTORY = 3;

    /**
     * This is a template for a view.
     */
    interface View extends BaseView<Presenter> {
        void showGames(List<Game> games);
        void setRefreshing(boolean isRefreshing);
    }

    /**
     * This is a template for a presenter.
     */
    interface Presenter extends BasePresenter {
        void loadGames(int type);
    }
}
