package com.snaptiongame.app.presentation.view.friends;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.snaptiongame.app.R;
import com.snaptiongame.app.SnaptionApplication;
import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.presentation.view.customviews.InsetDividerDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Tyler Wong
 */

public class FriendSearchActivity extends AppCompatActivity implements FriendsContract.View, SearchView.OnQueryTextListener {
    @BindView(R.id.searchback)
    ImageButton searchBack;
    @BindView(R.id.search_view)
    SearchView mSearchView;
    @BindView(R.id.search_results)
    RecyclerView mSearchResults;
    //@BindView(R.id.empty_view)
    //LinearLayout mEmptyView;


    private FriendsContract.Presenter mPresenter;

    private FriendsAdapter mAdapter;
    private InsetDividerDecoration mDecoration;

    private String oldText = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_search);
        ButterKnife.bind(this);
        mPresenter = new FriendsPresenter(this);

        mDecoration = new InsetDividerDecoration(
                FriendViewHolder.class,
                getResources().getDimensionPixelSize(R.dimen.divider_height),
                getResources().getDimensionPixelSize(R.dimen.keyline_1),
                ContextCompat.getColor(SnaptionApplication.getContext(), R.color.divider));
        mSearchResults.addItemDecoration(mDecoration);

        mAdapter = new FriendsAdapter(new ArrayList<>());
        mAdapter.setPresenter(mPresenter);
        mAdapter.setContextForDialog(SnaptionApplication.getContext());

        mSearchResults.setHasFixedSize(true);
        mSearchResults.setLayoutManager(new LinearLayoutManager(this));
        mSearchResults.setAdapter(mAdapter);
        mSearchView.setOnQueryTextListener(this);
        //mEmptyView.setVisibility(View.GONE);
        mSearchResults.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.clearFriends();
        if (newText.isEmpty())
            mPresenter.loadFriends();
        else
            mPresenter.findFriends(newText);
        return true;
    }

    @Override
    public void setPresenter(FriendsContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void processFriends(List<Friend> friends) {
        //if (friends.isEmpty()) {
       //     showEmptyView();
        //}
        //else {
            showFriendList();
            mAdapter.setFriends(friends);
        //}
    }

    @Override
    public void addFriend(Friend friend) {
        mAdapter.addFriend(friend);
    }

    @Override
    public void showEmptyView() {
        //mEmptyView.setVisibility(View.VISIBLE);
        mSearchResults.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showFriendList() {
        //mEmptyView.setVisibility(View.GONE);
        mSearchResults.setVisibility(View.VISIBLE);
    }

    @Override
    public void setRefreshing(boolean isRefreshing) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @OnClick(R.id.searchback)
    public void searchBack() {
        mAdapter.clearFriends();
        super.onBackPressed();
    }
}
