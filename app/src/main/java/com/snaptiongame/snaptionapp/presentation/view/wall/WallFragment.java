package com.snaptiongame.snaptionapp.presentation.view.wall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.authentication.AuthenticationManager;
import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.data.providers.SnaptionProvider;
import com.snaptiongame.snaptionapp.presentation.view.login.LoginActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Tyler Wong
 */

public class WallFragment extends Fragment {
   @BindView(R.id.fab)
   FloatingActionButton mFab;
   @BindView(R.id.wall)
   RecyclerView mWall;

   private AuthenticationManager mAuthManager;
   private WallAdapter mAdapter;
   private Unbinder mUnbinder;

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

      mWall.setLayoutManager(new LinearLayoutManager(getContext()));
      mAdapter = new WallAdapter(getContext(), null);
      mWall.setAdapter(mAdapter);

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

               }

               @Override
               public void onNext(List<Snaption> snaptions) {
                  mAdapter.setSnaptions(snaptions);
               }
            });
   }

   @OnClick(R.id.fab)
   public void createGame() {
      if (!mAuthManager.isLoggedIn()) {
         goToLogin();
      }
      else {
         Toast.makeText(getContext(), "This will lead to create game!", Toast.LENGTH_LONG).show();
      }
   }

   private void goToLogin() {
      Intent loginIntent = new Intent(getContext(), LoginActivity.class);
      startActivity(loginIntent);
   }

   @Override
   public void onDestroyView() {
      super.onDestroyView();
      mUnbinder.unbind();
   }
}
