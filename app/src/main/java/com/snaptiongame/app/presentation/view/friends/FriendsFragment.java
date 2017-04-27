package com.snaptiongame.app.presentation.view.friends;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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

import static com.snaptiongame.app.presentation.view.friends.FriendsDialogFragment.DialogToShow.STANDARD_DIALOG;

/**
 * @author Brian Gouldsberry
 */

public class FriendsFragment extends Fragment implements FriendsContract.View, FriendsDialogInterface {
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(getString(R.string.delete_pre) + " " + mAdapter.getFriends()
                            .get(index).username + " " + getString(R.string.delete_post))
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.yes), dialogClickListener)
                            .setNegativeButton(getString(R.string.no), dialogClickListener).show();
                }
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(mFriendsList);
        mPresenter.subscribe();

        return view;
    }

    public void inviteFriends() {
        mDialogFragmentDefault = FriendsDialogFragment.newInstance(STANDARD_DIALOG);
        mDialogFragmentDefault.setTargetFragment(this, 1);
        mDialogFragmentDefault.show(getFragmentManager(), "dialog");
        mDialogFragmentDefault.setDialogInterface(this, STANDARD_DIALOG);
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

    public void updateFriendsDialog(FriendsDialogFragment.DialogToShow dialogToShow) {
        mDialogFragmentDefault.dismiss();
        mDialogFragmentFriendSearch = FriendsDialogFragment.newInstance(dialogToShow);
        mDialogFragmentFriendSearch.setTargetFragment(this, 1);
        mDialogFragmentFriendSearch.show(getFragmentManager(), "dialog");
        mDialogFragmentFriendSearch.setDialogInterface(this, dialogToShow);
    }

    /**
     * This method determines what should be shown to a user after they click the negative
     * button on a dialog. For a standard dialog we just want to dismiss the dialog,
     * otherwise we return to the previous dialog
     *
     * @param whichDialog holder for the type of dialog currently being shown
     */
    public void negativeButtonClicked(FriendsDialogFragment.DialogToShow whichDialog) {
        if (whichDialog == STANDARD_DIALOG) {
            mDialogFragmentDefault.dismiss();
            if (mDialogFragmentFriendSearch != null)
                mDialogFragmentFriendSearch.dismiss();
        }
        else {
            mDialogFragmentFriendSearch.dismiss();
            mDialogFragmentDefault = FriendsDialogFragment.newInstance(STANDARD_DIALOG);
            mDialogFragmentDefault.setTargetFragment(this, 1);
            mDialogFragmentDefault.show(getFragmentManager(), "dialog");
        }
    }

    @Override
    public void setRefreshing(boolean isRefreshing) {
        mRefreshLayout.setRefreshing(isRefreshing);
    }

    @Override
    public void processFriends(List<Friend> friends) {
        if (friends.isEmpty()) {
            showEmptyView();
        }
        else {
            showFriendList();
            mAdapter.setFriends(friends);
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void addFriend(Friend friend) {

    }

    @Override
    public void setPresenter(FriendsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        FriendsDialogFragment.DialogToShow toShow = (FriendsDialogFragment.DialogToShow) data.getSerializableExtra("which");
        switch (resultCode) {
            case 1:
                negativeButtonClicked(toShow);
                break;
            case 2:
                updateFriendsDialog(toShow);
                break;
        }
    }
}
