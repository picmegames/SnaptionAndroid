package com.snaptiongame.snaptionapp.presentation.view.friends;

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
import com.snaptiongame.snaptionapp.presentation.view.friends.FriendsDialogFragment;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author Brian Gouldsberry
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
        View view = inflater.inflate(R.layout.friends_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mFriends.setHasFixedSize(true);
        mFriends.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new FriendsAdapter(getContext(), new ArrayList<>());
        mFriends.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuthManager = AuthenticationManager.getInstance(getContext());

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
}
