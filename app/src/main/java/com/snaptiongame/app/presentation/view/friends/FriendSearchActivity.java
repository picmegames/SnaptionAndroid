package com.snaptiongame.app.presentation.view.friends;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.presentation.view.decorations.InsetDividerDecoration;
import com.snaptiongame.app.presentation.view.listeners.InfiniteRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Tyler Wong
 */

public class FriendSearchActivity extends AppCompatActivity implements FriendsContract.View, SearchView.OnQueryTextListener {
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.search_results)
    RecyclerView searchResults;

    private FriendsContract.Presenter presenter;
    private InfiniteRecyclerViewScrollListener scrollListener;

    private String query;
    private FriendsAdapter adapter;
    private InsetDividerDecoration decoration;

    private static final String SEARCH_ICON = "android:id/search_mag_icon";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_search);
        ButterKnife.bind(this);
        presenter = new FriendsPresenter(this);

        decoration = new InsetDividerDecoration(
                FriendViewHolder.class,
                getResources().getDimensionPixelSize(R.dimen.divider_height),
                getResources().getDimensionPixelSize(R.dimen.keyline_1),
                ContextCompat.getColor(this, R.color.divider));
        searchResults.addItemDecoration(decoration);

        adapter = new FriendsAdapter(new ArrayList<>());
        adapter.setPresenter(presenter);
        adapter.setShouldDisplayAddRemoveOption(true);

        searchResults.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        searchResults.setLayoutManager(layoutManager);
        searchResults.setAdapter(adapter);
        searchView.setOnQueryTextListener(this);

        scrollListener = new InfiniteRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (query != null && !query.isEmpty()) {
                    presenter.findFriends(query, page);
                }
                else {
                    presenter.loadFriends(page);
                }
            }
        };

        searchResults.addOnScrollListener(scrollListener);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            int searchIconId = getResources().getIdentifier(SEARCH_ICON, null, null);
            ImageView searchIcon = ButterKnife.findById(this, searchIconId);
            if (searchIcon != null) {
                searchIcon.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        handleSearch(newText);
        return true;
    }

    private void handleSearch(String query) {
        this.query = query.trim();
        adapter.clear();
        scrollListener.resetState();

        if (this.query.isEmpty()) {
            presenter.subscribe();
        }
        else {
            presenter.findFriends(this.query, 1);
        }
    }

    private void showInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    private void hideInputMethod() {
        View view = getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void setPresenter(FriendsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void processFriends(List<Friend> friends) {
        showFriendList();
        adapter.addFriends(friends);
    }

    @Override
    public void addFriend(Friend friend) {
        adapter.addFriend(friend);
    }

    @Override
    public void showEmptyView() {
        searchResults.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showFriendList() {
        searchResults.setVisibility(View.VISIBLE);
    }

    @Override
    public void setRefreshing(boolean isRefreshing) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.clear();
        scrollListener.resetState();
        presenter.subscribe();
        showInputMethod();
        handleSearch(searchView.getQuery().toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideInputMethod();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unsubscribe();
    }

    @OnClick(R.id.searchback)
    public void searchBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
        searchResults.setVisibility(View.INVISIBLE);
    }
}
