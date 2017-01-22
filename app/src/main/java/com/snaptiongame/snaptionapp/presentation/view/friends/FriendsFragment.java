package com.snaptiongame.snaptionapp.presentation.view.friends;

/**
 * @author Brian Gouldsberry
 */

import android.app.DialogFragment;
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

import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.authentication.AuthenticationManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author Tyler Wong
 */

public class FriendsFragment extends Fragment {
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.friend_list)
    RecyclerView mFriends;

    private FriendsAdapter mAdapter;

    private AuthenticationManager  mAuthManager;
    private Unbinder mUnbinder;
    private DialogFragment mDialogFragmentDefault;
    private DialogFragment mDialogFragmentFriendSearch;


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

        mAdapter = new FriendsAdapter(getContext(), new ArrayList<>());
        mFriends.setAdapter(mAdapter);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.friends_label);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //loadFriends();
    }

    private void loadFriends() {
        if (mAuthManager.isLoggedIn()) {

        }
    }

    @OnClick(R.id.fab)
    public void inviteFriends() {


        mDialogFragmentDefault = new FriendsDialogFragment().newInstance(0, this);
        mDialogFragmentDefault.show(getActivity().getFragmentManager(), "dialog");


    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        mAuthManager.disconnectGoogleApi();
    }

    public void updateFriendsDialog(int whichOptionSelected) {


        android.support.v4.app.FragmentTransaction transaction =
                getActivity().getSupportFragmentManager().beginTransaction();
        mDialogFragmentDefault.dismiss();
        mDialogFragmentFriendSearch = new FriendsDialogFragment().newInstance(whichOptionSelected + 1, this);


        mDialogFragmentFriendSearch.show(getActivity().getFragmentManager(), "dialog");
    }

    public void negativeButtonClicked(int whichDialog) {

        switch (whichDialog) {

            //Default dialog with all options present
            case 0:
                mDialogFragmentDefault.dismiss();
                break;
            default:
                mDialogFragmentFriendSearch.dismiss();
                mDialogFragmentDefault.show(getActivity().getFragmentManager(), "dialog");
        }
    }
}
