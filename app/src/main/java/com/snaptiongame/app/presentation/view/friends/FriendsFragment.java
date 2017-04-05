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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.snaptiongame.app.R;
import com.snaptiongame.app.data.authentication.AuthenticationManager;
import com.snaptiongame.app.data.models.AddFriendRequest;
import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.data.providers.FriendProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static com.snaptiongame.app.presentation.view.friends.FriendsDialogFragment.DialogToShow.STANDARD_DIALOG;

/**
 * @author Brian Gouldsberry
 */

public class FriendsFragment extends Fragment implements FriendsContract.View, FriendsDialogInterface {
    @BindView(R.id.friend_list)
    RecyclerView mFriends;
    @BindView(R.id.query_field)
    EditText mQuery;
    @BindView(R.id.clear_button)
    Button clear;
    @BindView(R.id.refresh_layout_friends)
    SwipeRefreshLayout mRefreshLayout;

    protected FriendsContract.Presenter mPresenter;

    private FriendsAdapter mAdapter;
    private List<Friend> friends = new ArrayList<>();
    private String query = null;

    private AuthenticationManager mAuthManager;
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

        mAuthManager = AuthenticationManager.getInstance();
        mPresenter = new FriendsPresenter(this, mAuthManager.getUserId());

        mFriends.setHasFixedSize(true);
        mFriends.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new FriendsAdapter(friends);
        mFriends.setAdapter(mAdapter);

        mQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<Friend> results = filterList(friends, mQuery.getText().toString());
                mAdapter.setFriends(results);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        clear.setOnClickListener(theView -> {
            query = null;
            mQuery.setText("");
            mAdapter.setFriends(friends);
            mAdapter.notifyDataSetChanged();
        });

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
                            case DialogInterface.BUTTON_POSITIVE: //yes clicked
                                int id = mAdapter.getFriends().get(index).id;
                                removeFriend(id);
                                mAdapter.getFriends().remove(index);
                                mAdapter.notifyItemRemoved(index);
                                //Delete on backend
                                break;
                            case DialogInterface.BUTTON_NEGATIVE: //no clicked
                                //No button clicked
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

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(mFriends);
        mPresenter.subscribe();

        return view;
    }

    //Returns a subset of friends where each friend has the query in either their name or username
    public static List<Friend> filterList(List<Friend> friends, String query) {
        if (query != null && query.length() > 0) {
            ArrayList<Friend> filtered = new ArrayList<>();
            for (Friend pal : friends) {
                String mashedNames = pal.fullName + " " + pal.username;
                if (mashedNames.toLowerCase().contains(query.toLowerCase())) {
                    filtered.add(pal);
                }
            }
            return filtered;
        }
        return friends;
    }

    public void inviteFriends() {
        mDialogFragmentDefault = FriendsDialogFragment.newInstance(STANDARD_DIALOG);
        mDialogFragmentDefault.setTargetFragment(this, 1);
        mDialogFragmentDefault.show(getFragmentManager(), "dialog");
        mDialogFragmentDefault.setDialogInterface(this, STANDARD_DIALOG);
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

    private void removeFriend(int id) {
        FriendProvider.removeFriend(mAuthManager.getUserId(), new AddFriendRequest(id))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(request -> {
                }, Timber::e, () -> Timber.i("Successfully removed friend!"));
    }

    public List<Friend> getFriends() {
        return friends;
    }

    @Override
    public void processFriends(List<Friend> friends) {
        this.friends = friends;
        mAdapter.clearFriends();
        mAdapter.setFriends(filterList(this.friends, query));
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
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
