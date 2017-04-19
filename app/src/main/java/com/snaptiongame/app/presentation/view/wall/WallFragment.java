package com.snaptiongame.app.presentation.view.wall;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.utils.NetworkListener;
import com.snaptiongame.app.presentation.view.customviews.WallSpacesItemDecoration;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.snaptiongame.app.R.drawable.ic_signal_wifi_off_grey_800_24dp;
import static com.snaptiongame.app.R.drawable.snaption_icon_gray;

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
    RecyclerView mWall;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.empty_or_disconnected_view)
    LinearLayout mEmptyOrDisconnectedView;
    @BindView(R.id.wall_state_image)
    ImageView mWallStateImage;
    @BindView(R.id.wall_state)
    TextView mWallState;

    private WallContract.Presenter mPresenter;
    private NetworkListener mNetworkListener;

    private WallAdapter mAdapter;
    private Unbinder mUnbinder;
    private int mUserId;
    private int mType;
    public static final String TAG = WallFragment.class.getSimpleName();

    public static final int NUM_COLUMNS = 2;
    public static final String USER_ID = "userId";
    public static final String TYPE = "type";
    public static final int EMPTY_VIEW = 0;
    public static final int DISCONNECTED_VIEW = 1;


    /**
     * This method provides a new instance of a Wall Fragment.
     *
     * @return An instance of a Wall Fragment
     */
    public static WallFragment getInstance(int userId, int type) {
        Bundle args = new Bundle();
        args.putInt(USER_ID, userId);
        args.putInt(TYPE, type);
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
        mUnbinder = ButterKnife.bind(this, view);
        mUserId = getArguments().getInt(USER_ID);
        mType = getArguments().getInt(TYPE);
        mPresenter = new WallPresenter(this, mUserId, mType);
        mNetworkListener = new NetworkListener(getContext());
        getContext().registerReceiver(mNetworkListener, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

        mWall.setLayoutManager(new StaggeredGridLayoutManager(NUM_COLUMNS, StaggeredGridLayoutManager.VERTICAL));
        mWall.addItemDecoration(new WallSpacesItemDecoration(
                getContext().getResources().getDimensionPixelSize(R.dimen.item_spacing)));
        mWall.setHasFixedSize(true);

        mAdapter = new WallAdapter(new ArrayList<>());
        mWall.setAdapter(mAdapter);


        mRefreshLayout.setOnRefreshListener( () -> mPresenter.loadGames(mType, null));
        mRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getContext(), R.color.colorAccent)
        );

        if (mType == WallContract.DISCOVER) {
            mPresenter.subscribe();
        }

        return view;
    }

    /**
     * This method is called when the fragment comes into view.
     */
    @Override
    public void onResume() {
        super.onResume();

        // Do not refresh wall in onResume if on Discover tab
        // Need to subscribe if on Discover tab because onResume will
        // not subscribe if we are on Discover tab
        if (mType == WallContract.DISCOVER) {
            mPresenter.subscribe();
        }


    }

    /**
     * This method will filter the wall by tag
     *
     * @param tags a list of user defined tags to filter by
     */
    public void filterGames(List<String> tags) {
        mPresenter.loadGames(mType, tags);
    }

    /**
     * This method is called after the network call to get new
     * games is complete and successful.
     *
     * @param games A list of new games from the server
     */
    @Override
    public void showGames(List<Game> games) {
        if (games.isEmpty()) {
            showEmptyView();
        } else {
            showWall();
            mAdapter.setGames(games);
        }
    }

    @Override
    public void showEmptyView() {
        mEmptyOrDisconnectedView.setVisibility(View.VISIBLE);
        mWallStateImage.setImageResource(snaption_icon_gray);
        mWallState.setText(R.string.nothing_here);

        if (mType != WallContract.HISTORY) {
            mWall.setVisibility(View.GONE);
        }
    }

    @Override
    public void showDisconnectedView() {
        mEmptyOrDisconnectedView.setVisibility(View.VISIBLE);
        mWallStateImage.setImageResource(ic_signal_wifi_off_grey_800_24dp);
        mWallState.setText(R.string.no_internet_connection);

        if (mType != WallContract.HISTORY) {
            mWall.setVisibility(View.GONE);
        }
    }

    @Override
    public void showWall() {
        mEmptyOrDisconnectedView.setVisibility(View.GONE);

        if (mType != WallContract.HISTORY) {
            mWall.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This method sets a new presenter for the view.
     *
     * @param presenter The presenter for this view
     */
    @Override
    public void setPresenter(WallContract.Presenter presenter) {
        mPresenter = presenter;
    }

    /**
     * This method sets the layout to a refreshing
     * state.
     *
     * @param isRefreshing Whether or not the layout is refreshing
     */
    @Override
    public void setRefreshing(boolean isRefreshing) {
        mRefreshLayout.setRefreshing(isRefreshing);
    }

    /**
     * This method is called when the view is destroyed.
     * It will unbind and dispose of the Butterknife bindings
     * and network calls.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        mPresenter.unsubscribe();
    }
}
