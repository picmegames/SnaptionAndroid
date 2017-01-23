package com.snaptiongame.snaptionapp.presentation.view.friends;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.models.Friend;
import com.snaptiongame.snaptionapp.data.providers.FriendProvider;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by nickromero on 1/20/17.
 */

public class FriendsDialogFragment extends DialogFragment {


    private int mWhichDialog;

    private AlertDialog.Builder mDialogBuilder;

    private int mWhichLayoutToInflate;

    //Changes based on what dialog we are showing
    private String mDialogTitle;
    private int mHeaderIcon;

    private static FriendsFragment mFragmentActivity;
    private static String TAG = "FRIEND_DIALOGUE";


    private String sNegativeButtonText;
    private String sPositiveButtonText;


    private final String CANCEL = "Cancel";
    private final String BACK = "Back";
    private final String ADD_FRIEND_TITLE = "Add a friend!";
    private final String FIND_FRIEND = "Find your friends!";
    private final String INVITE_FRIEND_LONG = "Invite a friend to Snaption!";
    private final String INVITE_FRIEND_SHORT = "Invite Friend";
    private final String[] mHints = {"Ex: (555)-444-3333", "Ex: Bill Johnson", "Ex: sk8rdude@aol" +
            ".com"};
    private FriendsAdapter mAdapter;
    private RecyclerView mResults;


    public FriendsDialogFragment() {

    }

    static FriendsDialogFragment newInstance(int whichDialogToShow, FriendsFragment fragmentActivity) {
        FriendsDialogFragment newFragment = new FriendsDialogFragment();

        Bundle args = new Bundle();
        args.putInt("whichDialog", whichDialogToShow);
        newFragment.setArguments(args);

        mFragmentActivity = fragmentActivity;

        return newFragment;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO replace with constant key
        mWhichDialog = getArguments().getInt("whichDialog");
        sNegativeButtonText = BACK;
        sPositiveButtonText = INVITE_FRIEND_SHORT;
        mWhichLayoutToInflate = R.layout.custom_friend_dialog;
        mDialogTitle = FIND_FRIEND;

        switch (mWhichDialog) {

            //Handles the default Add Friend Dialog
            case 0:
                mWhichLayoutToInflate = R.layout.custom_friend_dialog;
                mDialogTitle = ADD_FRIEND_TITLE;
                mHeaderIcon = R.drawable.snaption_icon;
                sNegativeButtonText = CANCEL;
                sPositiveButtonText = INVITE_FRIEND_LONG;
                break;
            case 1:
                mHeaderIcon = R.drawable.ic_phone;
                break;
            case 2:
                mHeaderIcon = R.drawable.ic_facebook;
                break;
            case 3:
                mHeaderIcon = R.drawable.ic_email;
                break;
        }

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        mDialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View headerView = inflater.inflate(R.layout.dialog_header, null);
        TextView headerText = (TextView) headerView.findViewById(R.id.dialogTitleText);
        ImageView headerIcon = (ImageView) headerView.findViewById(R.id.dialogTitleHeaderIcon);

        //Set the title bar
        headerText.setText(mDialogTitle);
        headerIcon.setImageResource(mHeaderIcon);

        //Assign Title
        mDialogBuilder.setCustomTitle(headerView);


        /**
         * Inflate a view that holds the
         */
        if (mWhichDialog == 0) {
            ListView friendList = (ListView) inflater.inflate(R.layout.custom_friend_dialog, null);
            mDialogBuilder.setView(friendList);
            friendList.setAdapter(new AddFriendsAdapter(getActivity()));
        }
        /**
         * Change the following for your own screen on the invite dialog
         */
        else {
            View view = inflater.inflate(R.layout.find_friend_layout, null);
            mDialogBuilder.setView(view);
            EditText search = (EditText) view.findViewById(R.id.friendSearchView);
            search.setHint(mHints[mWhichDialog - 1]); //Sets the hint based off of which method
            // the user is adding a friend
             mResults = (RecyclerView) view.findViewById(R.id.search_results);
             mResults.setLayoutManager(new LinearLayoutManager(view.getContext()));
            ArrayList<Friend> friends = new ArrayList<>();
            if (mWhichDialog == 2) {
                //provide friends
                loadFacebookFriends();
            }
            mAdapter = new FriendsAdapter(view.getContext(), friends);
             mResults.setAdapter(mAdapter);
        }


        mDialogBuilder.setPositiveButton(sPositiveButtonText, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setNegativeButton(sNegativeButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((FriendsFragment) mFragmentActivity).negativeButtonClicked(mWhichDialog);
            }
        });


        return mDialogBuilder.create();

    }

    private void loadFacebookFriends() {
        FriendProvider.getFacebookFriends()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Friend>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Nope :(");
                    }

                    @Override
                    public void onNext(List<Friend> friends) {
                        mAdapter.setFriends(friends);
                    }
                });
    }

    private class AddFriendsAdapter extends BaseAdapter {

        private Context mContext;

        private final String[] mData = {"via Phone #", "via Facebook", "via Email"};


        private int[] mIconData = {R.drawable.ic_phone,
                R.drawable.ic_facebook,
                R.drawable.ic_email};

        private AddFriendsAdapter(Context context) {
            this.mContext = context;

        }

        @Override
        public int getCount() {
            return mData.length;
        }

        @Override
        public Object getItem(int position) {
            return mData[position];
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view = convertView;

            if (view == null)
                view = getActivity().getLayoutInflater().inflate(R.layout.friend_invite_option_layout, null);

            TextView inviteText = (TextView) view.findViewById(R.id.inviteOptionText);
            inviteText.setText(mData[position]);

            ImageView inviteImage = (ImageView) view.findViewById(R.id.inviteOptionIcon);
            inviteImage.setImageResource(mIconData[position]);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ((FriendsFragment) mFragmentActivity).updateFriendsDialog(position);
                }
            });


            return view;
        }
    }


}
