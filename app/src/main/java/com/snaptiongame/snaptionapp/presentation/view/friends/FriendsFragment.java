package com.snaptiongame.snaptionapp.presentation.view.friends;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.ImageView;

import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.authentication.AuthenticationManager;
import com.snaptiongame.snaptionapp.data.models.AddFriendRequest;
import com.snaptiongame.snaptionapp.data.models.Friend;
import com.snaptiongame.snaptionapp.data.providers.FriendProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * @author Brian Gouldsberry
 */

public class FriendsFragment extends Fragment implements FriendsContract.View {
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.friend_list)
    RecyclerView mFriends;
    @BindView(R.id.query_field)
    EditText mQuery;
    @BindView(R.id.clear_button)
    Button clear;
    @BindView(R.id.refresh_layout_friends)
    SwipeRefreshLayout mRefreshLayout;

    private FriendsContract.Presenter mPresenter;

    private FriendsAdapter mAdapter;
    private List<Friend> friends = new ArrayList<>();
    private String query = null;

    private AuthenticationManager  mAuthManager;
    private Unbinder mUnbinder;
    private DialogFragment mDialogFragmentDefault;
    private DialogFragment mDialogFragmentFriendSearch;

    public static FriendsFragment getInstance() {
        return new FriendsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.friends_fragment, container, false);


        mUnbinder = ButterKnife.bind(this, view);

        mAuthManager = AuthenticationManager.getInstance(getContext());
        mPresenter = new FriendsPresenter(this, mAuthManager.getSnaptionUserId());

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
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE: //yes clicked
                                int id = Integer.parseInt(mAdapter.getFriends().get(index).id);
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
                            .get(index).userName + " " + getString(R.string.delete_post))
                            .setPositiveButton(getString(R.string.yes), dialogClickListener)
                            .setNegativeButton(getString(R.string.no), dialogClickListener).show();
                }
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(mFriends);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.friends_label);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    //Returns a subset of friends where each friend has the query in either their name or username
    public static List<Friend> filterList(List<Friend> friends, String query) {
        if (query != null && query.length() > 0) {
            ArrayList<Friend> filtered = new ArrayList<>();
            for (Friend pal : friends) {
                String mashedNames = pal.fullName + " " + pal.userName;
                if (mashedNames.toLowerCase().contains(query.toLowerCase())) {
                    filtered.add(pal);
                }
            }
            return filtered;
        }
        return friends;
    }

    @OnClick(R.id.fab)
    public void inviteFriends() {


        mDialogFragmentDefault = new FriendsDialogFragment().newInstance(FriendsDialogFragment.DialogToShow.STANDARD_DIALOG, this);
        mDialogFragmentDefault.show(getActivity().getFragmentManager(), "dialog");


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        mAuthManager.disconnectGoogleApi();
    }

    public void updateFriendsDialog(int whichOptionSelected) {
        FriendsDialogFragment.DialogToShow dialogToShow = null;
        android.support.v4.app.FragmentTransaction transaction =
                getActivity().getSupportFragmentManager().beginTransaction();
        mDialogFragmentDefault.dismiss();

        /**
         * Depending on which invite option, the user selects, we want to show them the correct
         * dialog
         */
        switch (whichOptionSelected) {
            case 0:
                dialogToShow = FriendsDialogFragment.DialogToShow.PHONE_INVITE;
                break;
            case 1:
                dialogToShow = FriendsDialogFragment.DialogToShow.FACEBOOK_INVITE;
                break;
            case 2:
                dialogToShow = FriendsDialogFragment.DialogToShow.EMAIL_INVITE;
                break;
        }

        mDialogFragmentFriendSearch = new FriendsDialogFragment().newInstance(dialogToShow, this);


        mDialogFragmentFriendSearch.show(getActivity().getFragmentManager(), "dialog");
    }

    /**
     * This method determines what should be shown to a user after they click the negative
     * button on a dialog. For a standard dialog we just want to dismiss the dialog,
     * otherwise we return to the previous dialog
     * @param whichDialog holder for the type of dialog currently being shown
     */
    public void negativeButtonClicked(FriendsDialogFragment.DialogToShow whichDialog) {

        switch (whichDialog) {

            //Default dialog with all options present
            case STANDARD_DIALOG:
                mDialogFragmentDefault.dismiss();
                break;
            default:
                mDialogFragmentFriendSearch.dismiss();
                mDialogFragmentDefault.show(getActivity().getFragmentManager(), "dialog");
                break;
        }
    }

    private void removeFriend(int id) {
        FriendProvider.removeFriend(mAuthManager.getSnaptionUserId(), new AddFriendRequest(id))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(request -> {
                }, Timber::e, () -> Timber.i("Successfully removed friend!"));
    }

    @Override
    public void processFriends(List<Friend> friends) {
        this.friends = friends;
        mAdapter.clearFriends();
        mAdapter.setFriends(filterList(this.friends, query));
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setPresenter(FriendsContract.Presenter presenter) {
        mPresenter = presenter;
    }


}
