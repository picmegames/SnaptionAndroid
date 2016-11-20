package com.snaptiongame.snaptionapp.presentation.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.auth.AuthenticationManager;
import com.snaptiongame.snaptionapp.presentation.view.activities.LoginActivity;
import com.snaptiongame.snaptionapp.presentation.view.activities.MainActivity;
import com.snaptiongame.snaptionapp.presentation.view.customviews.AnimatedRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author Tyler Wong
 */

public class WallFragment extends Fragment {
   @BindView(R.id.fab)
   FloatingActionButton mFab;
   @BindView(R.id.wall)
   AnimatedRecyclerView mWall;

   private AuthenticationManager mAuthManager;
   private Unbinder mUnbinder;

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      super.onCreateView(inflater, container, savedInstanceState);
      mAuthManager = AuthenticationManager.getInstance(getContext());

      View view = inflater.inflate(R.layout.wall_fragment, container, false);
      mUnbinder = ButterKnife.bind(this, view);

      mWall.setHasFixedSize(true);
      LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
      layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
      mWall.setLayoutManager(layoutManager);

      ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
      if (actionBar != null) {
         actionBar.setTitle(R.string.wall_label);
      }

      return view;
   }

   @OnClick(R.id.fab)
   public void createGame(View view) {
      Context context = getContext();

      if (!mAuthManager.isLoggedIn()) {
         mAuthManager.registerCallback(this::goToMain);
         goToLogin();
      }
      else {
         Toast.makeText(context, "This will lead to create game!", Toast.LENGTH_LONG).show();
      }
   }

   private void goToMain() {
      Intent mainIntent = new Intent(getContext(), MainActivity.class);
      startActivity(mainIntent);
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
