package edu.calpoly.csc.snaptionverticalprototype.presentation.view.fragments;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import edu.calpoly.csc.snaptionverticalprototype.R;
import edu.calpoly.csc.snaptionverticalprototype.presentation.view.customviews.AnimatedRecyclerView;

/**
 * @author Tyler Wong
 */

public class WallFragment extends Fragment {
   @BindView(R.id.fab)
   FloatingActionButton mFab;
   @BindView(R.id.wall)
   AnimatedRecyclerView mWall;

   private Unbinder mUnbinder;

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      super.onCreateView(inflater, container, savedInstanceState);
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

   @Override
   public void onDestroyView() {
      super.onDestroyView();
      mUnbinder.unbind();
   }
}
