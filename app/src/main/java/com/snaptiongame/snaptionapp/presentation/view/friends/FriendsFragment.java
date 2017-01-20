package com.snaptiongame.snaptionapp.presentation.view.friends;

/**
 * Created by BrianGouldsberry on 1/19/17.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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
import com.snaptiongame.snaptionapp.presentation.view.wall.SpacesItemDecoration;
import com.snaptiongame.snaptionapp.presentation.view.wall.WallAdapter;

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

public class FriendsFragment extends Fragment {
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.friend_list)
    RecyclerView mFriends;

    private AuthenticationManager mAuthManager;
    private WallAdapter mAdapter;
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.friends_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuthManager = AuthenticationManager.getInstance(getContext());
        mUnbinder = ButterKnife.bind(this, view);

        mFriends.setLayoutManager(new LinearLayoutManager(getContext()));
//        mFriends.addItemDecoration(new SpacesItemDecoration(
//                getContext().getResources().getDimensionPixelSize(R.dimen.item_spacing)));

        mAdapter = new WallAdapter(getContext(), new ArrayList<>());
        mFriends.setAdapter(mAdapter);


        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.friends_label);
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
                    }
                });
    }

    @OnClick(R.id.fab)
    public void createGame() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        mAuthManager.unregisterCallback();
    }
}