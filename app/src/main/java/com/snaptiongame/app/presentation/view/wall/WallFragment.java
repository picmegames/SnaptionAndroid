package com.snaptiongame.app.presentation.view.wall;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.presentation.view.decorations.WallSpacesItemDecoration;
import com.snaptiongame.app.presentation.view.listeners.InfiniteRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * The Wall Fragment is a fragment that shows the wall to the user.
 * When the wall is first shown it will load in a list of games
 * from the server.
 *
 * @author Tyler Wong
 * @version 1.0
 */
public class WallFragment extends Fragment implements WallContract.View {
    @BindView(R.id.wall)
    RecyclerView wall;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.empty_or_disconnected_view)
    LinearLayout emptyOrDisconnectedView;
    @BindView(R.id.wall_state_image)
    ImageView wallStateImage;
    @BindView(R.id.wall_state)
    TextView wallState;

    private WallContract.Presenter presenter;
    private WallAdapter adapter;
    private Unbinder unbinder;
    private RecyclerView.LayoutManager currentLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private WallSpacesItemDecoration itemSpacesDecoration;
    private InfiniteRecyclerViewScrollListener scrollListener;
    private List<String> tags;
    private String status;
    private int userId;
    private int type;
    private int space;

    public static final String TAG = WallFragment.class.getSimpleName();

    public static final int NUM_COLUMNS = 2;
    public static final String USER_ID = "userId";
    public static final String TYPE = "type";
    public static final String IS_LIST = "isList";

    /**
     * This method provides a new instance of a Wall Fragment.
     *
     * @return An instance of a Wall Fragment
     */
    public static WallFragment getInstance(int userId, int type, boolean isList) {
        Bundle args = new Bundle();
        args.putInt(USER_ID, userId);
        args.putInt(TYPE, type);
        args.putBoolean(IS_LIST, isList);
        WallFragment wallFragment = new WallFragment();
        wallFragment.setArguments(args);
        return wallFragment;
    }

    /**
     * This method handles the instantiation of the view and its children.
     * It also sets up the Wall's Recycler View and its adapter as well as
     * the Wall Presenter.
     *
     * @param inflater           The inflater that inflates the view
     * @param container          The view group that the fragment is in
     * @param savedInstanceState The saved state of the fragment if any
     * @return A view that contains the new fragment's view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.wall_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        userId = getArguments().getInt(USER_ID);
        type = getArguments().getInt(TYPE);
        boolean isList = getArguments().getBoolean(IS_LIST);
        presenter = new WallPresenter(this, userId, type);
        linearLayoutManager = new LinearLayoutManager(getContext());
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, StaggeredGridLayoutManager.VERTICAL);

        setLayoutManager(isList);

        itemSpacesDecoration = new WallSpacesItemDecoration(space, isList);
        wall.addItemDecoration(itemSpacesDecoration);
        wall.setHasFixedSize(true);

        adapter = new WallAdapter(new ArrayList<>());
        adapter.setIsList(isList);
        wall.setAdapter(adapter);

        scrollListener = new InfiniteRecyclerViewScrollListener(currentLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.loadGames(type, tags, status, page);
            }
        };

        wall.addOnScrollListener(scrollListener);

        refreshLayout.setOnRefreshListener(this::refreshWall);

        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getContext(), R.color.colorAccent)
        );

        presenter.subscribe();

        return view;
    }

    public void refreshWall() {
        adapter.clear();
        scrollListener.resetState();
        presenter.subscribe();
    }

    public void switchLayout(boolean isList) {
        if (adapter != null && wall != null && itemSpacesDecoration != null) {
            adapter.setIsList(isList);
            wall.setAdapter(adapter);
            setLayoutManager(isList);

            scrollListener.setLayoutManager(currentLayoutManager);
            itemSpacesDecoration.setIsList(isList);
            itemSpacesDecoration.setSpacing(space);
        }
    }

    private void setLayoutManager(boolean isList) {
        if (isList) {
            currentLayoutManager = linearLayoutManager;
            wall.setLayoutManager(currentLayoutManager);
            space = getContext().getResources().getDimensionPixelSize(R.dimen.item_spacing_list);
        }
        else {
            currentLayoutManager = staggeredGridLayoutManager;
            wall.setLayoutManager(currentLayoutManager);
            space = getContext().getResources().getDimensionPixelSize(R.dimen.item_spacing_grid);
        }
    }

    /**
     * This method will filter the wall by tag and status (open/closed)
     *
     * @param tags a list of user defined tags to filter by
     * @param status open or closed
     */
    public void filterGames(List<String> tags, String status) {
        setRefreshing(true);
        adapter.clear();
        this.tags = tags;
        this.status = status;
        scrollListener.resetState();
        presenter.loadGames(type, this.tags, this.status, 1);
    }

    public boolean hasTags() {
        return tags != null && !tags.isEmpty();
    }

    /**
     * This method is called after the network call to get new
     * games is complete and successful.
     *
     * @param games A list of new games from the server
     */
    @Override
    public void showGames(List<Game> games) {
        adapter.addGames(games);

        if (!adapter.isEmpty()) {
            showWall();
        }
        else {
            showEmptyView();
        }
    }

    @Override
    public void showEmptyView() {
        adapter.clear();

        if (emptyOrDisconnectedView != null) {
            emptyOrDisconnectedView.setVisibility(View.VISIBLE);
        }

        if (wallState != null && wallStateImage != null) {
            wallState.setText(R.string.nothing_here);
            wallStateImage.setImageResource(R.drawable.snaption_icon_gray);
        }
    }

    @Override
    public void showDisconnectedView() {
        adapter.clear();

        if (emptyOrDisconnectedView != null) {
            emptyOrDisconnectedView.setVisibility(View.VISIBLE);
        }

        if (wallState != null && wallStateImage != null) {
            wallState.setText(R.string.no_internet);
            wallStateImage.setImageResource(R.drawable.ic_signal_wifi_off_grey_600_48dp);
        }
    }

    @Override
    public void showWall() {
        if (emptyOrDisconnectedView != null) {
            emptyOrDisconnectedView.setVisibility(View.GONE);
        }
    }

    /**
     * This method sets a new presenter for the view.
     *
     * @param presenter The presenter for this view
     */
    @Override
    public void setPresenter(WallContract.Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * This method sets the layout to a refreshing
     * state.
     *
     * @param isRefreshing Whether or not the layout is refreshing
     */
    @Override
    public void setRefreshing(boolean isRefreshing) {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(isRefreshing);
        }
    }

    /**
     * This method is called when the view is destroyed.
     * It will unbind and dispose of the Butterknife bindings
     * and network calls.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unsubscribe();
    }
}
