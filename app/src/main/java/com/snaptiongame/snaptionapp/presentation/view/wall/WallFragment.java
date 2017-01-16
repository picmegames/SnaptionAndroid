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
import android.util.Log;
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
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

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

   public static final int NUM_COLUMNS = 2;

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      super.onCreateView(inflater, container, savedInstanceState);
      return inflater.inflate(R.layout.wall_fragment, container, false);
   }

   @Override
   public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      mAuthManager = AuthenticationManager.getInstance(getContext());
      mUnbinder = ButterKnife.bind(this, view);

      mWall.setLayoutManager(new StaggeredGridLayoutManager(NUM_COLUMNS, StaggeredGridLayoutManager.VERTICAL));
      mWall.addItemDecoration(new SpacesItemDecoration(
            getContext().getResources().getDimensionPixelSize(R.dimen.item_spacing)));

      mAdapter = new WallAdapter(getContext(), new ArrayList<>());
      mWall.setAdapter(mAdapter);

      mRefreshLayout.setOnRefreshListener(this::loadSnaptions);

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
      SnaptionProvider.getAllSnaptions()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<List<Snaption>>() {
               @Override
               public void onCompleted() {

               }

               @Override
               public void onError(Throwable e) {
                  Log.e(TAG, "Nope :(");
               }

               @Override
               public void onNext(List<Snaption> snaptions) {
                  mAdapter.setSnaptions(snaptions);
                  mRefreshLayout.setRefreshing(false);
               }
            });
//      List<Snaption> snaptions = new ArrayList<>();
//      snaptions.add(new Snaption(0, new SnaptionMeta(0, 0, false, "", 0, "", "http://a.dilcdn" +
//              ".com/bl/wp-content/uploads/sites/6/2015/10/tfa_poster_wide_header-1536x864" +
//              "-959818851016.jpg", "", "", "Brian Gouldsberry", "Wow thats a " +
//              "reaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
//            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaallly cool lightning sword")));
//      snaptions.add(new Snaption(0, new SnaptionMeta(0, 0, false, "", 0, "", "https://upload" +
//              ".wikimedia.org/wikipedia/en/9/99/MarioSMBW.png", "", "", "Nick Romero", "6/10 Moustache")));
//      snaptions.add(new Snaption(0, new SnaptionMeta(0, 0, false, "", 0, "", "https://i.ytimg.com/vi" +
//            "/3soHkrdTdRQ/maxresdefault.jpg", "", "", "Tyler Wong", "I want to play as " +
//              "the little rat")));
//      snaptions.add(new Snaption(0, new SnaptionMeta(0, 0, false, "", 0, "", "https://www" +
//              ".sideshowtoy.com/photo_902292_thumb.jpg", "", "", "Quang Ngo", "What a cool storm trooper")));
//      snaptions.add(new Snaption(0, new SnaptionMeta(0, 0, false, "", 0, "",
//            "https://i.ytimg.com/vi/Ci3uHxJE59I/hqdefault.jpg", "", "", "", "")));
//      snaptions.add(new Snaption(0, new SnaptionMeta(0, 0, false, "", 0, "", "http://a.dilcdn" +
//            ".com/bl/wp-content/uploads/sites/6/2015/10/tfa_poster_wide_header-1536x864" +
//            "-959818851016.jpg", "", "", "Brian Gouldsberry", "Wow thats a " +
//            "reaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
//            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaallly cool lightning sword")));
//      snaptions.add(new Snaption(0, new SnaptionMeta(0, 0, false, "", 0, "", "https://upload" +
//            ".wikimedia.org/wikipedia/en/9/99/MarioSMBW.png", "", "", "Nick Romero", "6/10 Moustache")));
//      snaptions.add(new Snaption(0, new SnaptionMeta(0, 0, false, "", 0, "", "https://i.ytimg.com/vi" +
//            "/3soHkrdTdRQ/maxresdefault.jpg", "", "", "Tyler Wong", "I want to play as " +
//            "the little rat")));
//      snaptions.add(new Snaption(0, new SnaptionMeta(0, 0, false, "", 0, "", "https://www" +
//            ".sideshowtoy.com/photo_902292_thumb.jpg", "", "", "Quang Ngo", "What a cool storm trooper")));
//      snaptions.add(new Snaption(0, new SnaptionMeta(0, 0, false, "", 0, "", "https://i.ytimg.com/" +
//            "vi/Ci3uHxJE59I/hqdefault.jpg", "", "", "", "")));
//      snaptions.add(new Snaption(0, new SnaptionMeta(0, 0, false, "", 0, "", "http://a.dilcdn" +
//            ".com/bl/wp-content/uploads/sites/6/2015/10/tfa_poster_wide_header-1536x864" +
//            "-959818851016.jpg", "", "", "Brian Gouldsberry", "Wow thats a " +
//            "reaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
//            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaallly cool lightning sword")));
//      snaptions.add(new Snaption(0, new SnaptionMeta(0, 0, false, "", 0, "", "https://upload" +
//            ".wikimedia.org/wikipedia/en/9/99/MarioSMBW.png", "", "", "Nick Romero", "6/10 Moustache")));
//      snaptions.add(new Snaption(0, new SnaptionMeta(0, 0, false, "", 0, "", "https://i.ytimg.com/vi" +
//            "/3soHkrdTdRQ/maxresdefault.jpg", "", "", "Tyler Wong", "I want to play as " +
//            "the little rat")));
//      snaptions.add(new Snaption(0, new SnaptionMeta(0, 0, false, "", 0, "", "https://www" +
//            ".sideshowtoy.com/photo_902292_thumb.jpg", "", "", "Quang Ngo", "What a cool storm trooper")));
//      snaptions.add(new Snaption(0, new SnaptionMeta(0, 0, false, "", 0, "",
//            "https://i.ytimg.com/vi/Ci3uHxJE59I/hqdefault.jpg", "", "", "", "")));
//      mAdapter.setSnaptions(snaptions);
//      mRefreshLayout.setRefreshing(false);
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
      mAuthManager.unregisterCallback();
   }
}
