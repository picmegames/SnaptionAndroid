package com.snaptiongame.snaptionapp.presentation.view.wall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.authentication.AuthenticationManager;
import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.presentation.view.creategame.CreateGameActivity;
import com.snaptiongame.snaptionapp.presentation.view.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
   @BindView(R.id.fab)
   FloatingActionButton mFab;
   @BindView(R.id.wall)
   RecyclerView mWall;
   @BindView(R.id.refresh_layout)
   SwipeRefreshLayout mRefreshLayout;

   private WallContract.Presenter mPresenter;

   private AuthenticationManager mAuthManager;
   private WallAdapter mAdapter;
   private Unbinder mUnbinder;

   public static final int NUM_COLUMNS = 2;
   public static final int ITEM_VIEW_CACHE_SIZE = 20;

   /**
    * This method provides a new instance of a Wall Fragment.
    *
    * @return An instance of a Wall Fragment
    */
   public static WallFragment getInstance() {
      return new WallFragment();
   }

   /**
    * This method handles the instantiation of the view and its children.
    * It also sets up the Wall's Recycler View and its adapter as well as
    * the Wall Presenter.
    *
    * @param inflater The inflater that inflates the view
    * @param container The view group that the fragment is in
    * @param savedInstanceState The saved state of the fragment if any
    * @return A view that contains the new fragment's view
    */
   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      super.onCreateView(inflater, container, savedInstanceState);
      View view = inflater.inflate(R.layout.wall_fragment, container, false);
      mUnbinder = ButterKnife.bind(this, view);
      mPresenter = new WallPresenter(this);
      mAuthManager = AuthenticationManager.getInstance(getContext());

      mWall.setLayoutManager(new StaggeredGridLayoutManager(NUM_COLUMNS, StaggeredGridLayoutManager.VERTICAL));
      mWall.addItemDecoration(new SpacesItemDecoration(
            getContext().getResources().getDimensionPixelSize(R.dimen.item_spacing)));

      mWall.setHasFixedSize(true);
      mWall.setItemViewCacheSize(ITEM_VIEW_CACHE_SIZE);
      mWall.setDrawingCacheEnabled(true);
      mWall.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

      mAdapter = new WallAdapter(new ArrayList<>());
      mWall.setAdapter(mAdapter);

      mRefreshLayout.setOnRefreshListener(mPresenter::loadGames);

      return view;
   }

   /**
    * This method is called when the view becomes visible to the user.
    * This will reload the list of games.
    */
   @Override
   public void onResume() {
      super.onResume();
      mPresenter.subscribe();
      mRefreshLayout.setRefreshing(true);
   }

   /**
    * This method is called when the view goes into the background.
    * This will clear any disposable network calls.
    */
   @Override
   public void onPause() {
      super.onPause();
      mPresenter.unsubscribe();
   }

   /**
    * This method is called after the network call to get new
    * games is complete and successful.
    *
    * @param snaptions A list of new games from the server
    */
   @Override
   public void showGames(List<Snaption> snaptions) {
      mAdapter.setSnaptions(snaptions);
      mRefreshLayout.setRefreshing(false);
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
    * This method is called when a user clicks the
    * floating action button on the wall. If the user is
    * logged in, they will be directed to the create game
    * view. If not, they will be directed to the login
    * view.
    */
   @OnClick(R.id.fab)
   public void createGame() {
      if (!mAuthManager.isLoggedIn()) {
         goToLogin();
      }
      else {
         goToCreateGame();
      }
   }

   /**
    * This method creates and fires a new intent to switch to
    * a CreateGame activity.
    */
   private void goToCreateGame() {
      Intent createGameIntent = new Intent(getContext(), CreateGameActivity.class);
      startActivity(createGameIntent);
   }

   /**
    * This method creates and fires a new intent to switch to
    * a Login activity.
    */
   private void goToLogin() {
      Intent loginIntent = new Intent(getContext(), LoginActivity.class);
      startActivity(loginIntent);
   }

   /**
    * This method is called when the view is destroyed.
    * It will unbind and dispose of the Butterknife bindings.
    */
   @Override
   public void onDestroyView() {
      super.onDestroyView();
      mUnbinder.unbind();
   }
}
