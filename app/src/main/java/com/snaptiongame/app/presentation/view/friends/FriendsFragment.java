package com.snaptiongame.app.presentation.view.friends;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.presentation.view.customviews.InsetDividerDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Brian Gouldsberry
 */

public class FriendsFragment extends Fragment implements FriendsContract.View {
    @BindView(R.id.friend_list)
    RecyclerView mFriendsList;
    @BindView(R.id.refresh_layout_friends)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.empty_view)
    LinearLayout mEmptyView;

    protected FriendsContract.Presenter mPresenter;

    private FriendsAdapter mAdapter;
    private InsetDividerDecoration mDecoration;

    private Unbinder mUnbinder;
    private FriendsDialogFragment mDialogFragmentDefault;
    private FriendsDialogFragment mDialogFragmentFriendSearch;

    public static final String TAG = FriendsFragment.class.getSimpleName();

    public static FriendsFragment getInstance() {
        return new FriendsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.friends_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mPresenter = new FriendsPresenter(this);

        mDecoration = new InsetDividerDecoration(
                FriendViewHolder.class,
                getResources().getDimensionPixelSize(R.dimen.divider_height),
                getResources().getDimensionPixelSize(R.dimen.keyline_1),
                ContextCompat.getColor(getContext(), R.color.divider));
        mFriendsList.addItemDecoration(mDecoration);

        mFriendsList.setHasFixedSize(true);
        mFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new FriendsAdapter(new ArrayList<>(), mPresenter);
        mFriendsList.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(mPresenter::loadFriends);
        mRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getContext(), R.color.colorAccent)
        );

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    int index = viewHolder.getAdapterPosition();
                    DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                int id = mAdapter.getFriends().get(index).id;
                                mPresenter.removeFriend(id);
                                mAdapter.getFriends().remove(index);
                                mAdapter.notifyItemRemoved(index);
                                if (mAdapter.getFriends().isEmpty()) {
                                    showEmptyView();
                                }
                                else {
                                    showFriendList();
                                }
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                mAdapter.notifyItemChanged(index);
                                break;
                        }
                    };
                }
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(mFriendsList);
        mPresenter.subscribe();

        return view;
    }

    @Override
    public void showEmptyView() {
        mEmptyView.setVisibility(View.VISIBLE);
        mFriendsList.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showFriendList() {
        mEmptyView.setVisibility(View.GONE);
        mFriendsList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        mPresenter.unsubscribe();
    }

    public void inviteFriends() {
    }

    @Override
    public void setRefreshing(boolean isRefreshing) {
        mRefreshLayout.setRefreshing(isRefreshing);
    }

    @Override
    public void processFriends(List<Friend> friends) {
        //if (friends.isEmpty()) {
        //    showEmptyView();
        //}
        //else {
            showFriendList();
            mAdapter.setFriends(friends);
            mRefreshLayout.setRefreshing(false);
        //}
    }

    @Override
    public void addFriend(Friend friend) {
        mPresenter.addFriend(friend.id);
    }

    @Override
    public void setPresenter(FriendsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    /**
     *
     private void sendInviteIntent() {
     String smsBody = getString(R.string.invite_message) +
     getString(R.string.store_url);
     Intent inviteIntent = new Intent(Intent.ACTION_SEND);
     inviteIntent.putExtra(Intent.EXTRA_TEXT, smsBody);
     inviteIntent.setType("text/plain");
     Intent chooser = Intent.createChooser(inviteIntent, getString(R.string.invite_friend_via));
     startActivity(chooser);
     }
     */
}
