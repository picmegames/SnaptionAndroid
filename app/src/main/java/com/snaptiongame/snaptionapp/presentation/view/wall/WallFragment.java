package com.snaptiongame.snaptionapp.presentation.view.wall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.authentication.AuthenticationManager;
import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.data.providers.SnaptionProvider;
import com.snaptiongame.snaptionapp.presentation.view.creategame.CreateGame;
import com.snaptiongame.snaptionapp.presentation.view.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class WallFragment extends Fragment {
   @BindView(R.id.fab)
   FloatingActionButton mFab;
   @BindView(R.id.wall)
   RecyclerView mWall;
   @BindView(R.id.refresh_layout)
   SwipeRefreshLayout mRefreshLayout;

   private AuthenticationManager mAuthManager;
   private WallAdapter mAdapter;
   private Unbinder mUnbinder;
   private Subscription mSubscription;

   public static final int NUM_COLUMNS = 2;

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      super.onCreateView(inflater, container, savedInstanceState);
      View view = inflater.inflate(R.layout.wall_fragment, container, false);
      mUnbinder = ButterKnife.bind(this, view);

      mWall.setHasFixedSize(true);
      mWall.setLayoutManager(new StaggeredGridLayoutManager(NUM_COLUMNS, StaggeredGridLayoutManager.VERTICAL));
      mWall.addItemDecoration(new SpacesItemDecoration(
            getContext().getResources().getDimensionPixelSize(R.dimen.item_spacing)));

      mAdapter = new WallAdapter(getContext(), new ArrayList<>());
      mWall.setAdapter(mAdapter);

      mRefreshLayout.setOnRefreshListener(this::loadSnaptions);

      return view;
   }

   @Override
   public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      mAuthManager = AuthenticationManager.getInstance(getContext());

      ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
      if (actionBar != null) {
         actionBar.setTitle(R.string.wall_label);
      }
   }

   @Override
   public void onResume() {
      super.onResume();
      loadSnaptions();
   }

   private void loadSnaptions() {
      mSubscription = SnaptionProvider.getAllSnaptions()
            .publish(network ->
                  Observable.merge(network,
                        SnaptionProvider.getAllLocalSnaptions()
                              .takeUntil(network))
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<List<Snaption>>() {
               @Override
               public void onCompleted() {
                  Timber.i("Getting Snaptions completed successfully");
               }

               @Override
               public void onError(Throwable e) {
                  e.printStackTrace();
                  Timber.e(e, "Getting Snaptions errored.");
               }

               @Override
               public void onNext(List<Snaption> snaptions) {
                  try (Realm realmInstance = Realm.getDefaultInstance()) {
                     realmInstance.executeTransaction(realm ->
                           realmInstance.copyToRealmOrUpdate(snaptions));
                  }
                  mAdapter.clearSnaptions();
                  mAdapter.setSnaptions(snaptions);
                  mRefreshLayout.setRefreshing(false);
               }
            });
   }

   @OnClick(R.id.fab)
   public void createGame() {
      if (!mAuthManager.isLoggedIn()) {
         goToLogin();
      }
      else {
         goToCreateGame();
      }
   }

   private void goToCreateGame() {
      Intent createGameIntent = new Intent(getContext(), CreateGame.class);
      startActivity(createGameIntent);
   }

   private void goToLogin() {
      Intent loginIntent = new Intent(getContext(), LoginActivity.class);
      startActivity(loginIntent);
   }

   @Override
   public void onDestroyView() {
      super.onDestroyView();
      mUnbinder.unbind();
      mSubscription.unsubscribe();
      mAuthManager.unregisterCallback();
   }
}
