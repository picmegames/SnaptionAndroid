package com.snaptiongame.snaptionapp.presentation.view.friends;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.authentication.AuthenticationManager;
import com.snaptiongame.snaptionapp.data.models.AddFriendRequest;
import com.snaptiongame.snaptionapp.data.models.Friend;
import com.snaptiongame.snaptionapp.data.models.User;
import com.snaptiongame.snaptionapp.data.providers.FriendProvider;
import com.snaptiongame.snaptionapp.data.providers.UserProvider;
import com.snaptiongame.snaptionapp.data.providers.api.SnaptionApiProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;

/**
 * Created by nickromero on 1/20/17.
 * A FriendsDialogFragment is used to display a pop up dialog to a user whoe
 * is inviting friends into their profile, or into snaption. The dialog
 * provides various ways for a user to invite friends, such as via phone, email
 * facebook, etc.
 */

public class FriendsDialogFragment extends DialogFragment {

    /**
     * A representation of what dialog to show. Enum is used for readability.
     */
    public enum DialogToShow {
        PHONE_INVITE, FACEBOOK_INVITE, EMAIL_INVITE,
        STANDARD_DIALOG
    }

    /**
     * Holder for the type of dialog to show a user
     */
    private DialogToShow mWhichDialog;

    /**
     * Builder object used to create and show a dialog to a user
     */
    private AlertDialog.Builder mDialogBuilder;


    /**
     * Title of a dialog that changes, depending on which dialog is shown
     */
    private String mDialogTitle;

    /**
     * Header icon associated with a dialog title. Changes depending on which dialog is shown
     */
    private int mHeaderIcon;

    /**
     * Reference to the dialog fragment's parent activity. This is needed to call update methods
     * from the FriendsFragment
     */
    private static FriendsFragment mFragmentActivity;
    private static String TAG = "FRIEND_DIALOGUE";

    SnaptionApiProvider friendProvider;

    /**
     * Reference to the search view that is shown on the second dialog. Pulled out of local scope
     * from within the dialog to show option. This way we can access the field in any dialof or method.
     */
    private EditText search;

    /**
     * Member variables used to create the two buttons at the bottom of a dialog
     */
    private String sNegativeButtonText;
    private String sPositiveButtonText;


    private final String CANCEL = "Cancel";
    private final String BACK = "Back";
    private final String ADD_FRIEND_TITLE = "Add a friend!";
    private final String FIND_FRIEND = "Find your friends!";
    private final String INVITE_FRIEND_LONG = "Invite a friend to Snaption!";
    private final String INVITE_FRIEND_SHORT = "Invite Friend";
    private final String[] mHints = {"Ex: (555)-444-3333", "Ex: Bill Johnson", "Ex: sk8rdude@aol.com"};

    //private final InputType EMAIL_INPUT = InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS;


    /**
     * Custom adapter used to display the various options to invite friends
     */
    private FriendsAdapter mAdapter;

    /**
     * Recycler view used to hold the results of a search query run by the user
     */
    private RecyclerView mResults;

    /**
     * Hint to show a user when they are searching for a friend using one of the available methods
     */

    private String sHint;

    /**
     * ID of a user to possibly add as a friend
     */
    private int sUserID;

    private AuthenticationManager mAuthManager;

    /**
     * Empty constructor for the dialog. Expected from a DialogFragment
     */
    public FriendsDialogFragment() {
        friendProvider = new SnaptionApiProvider();
    }


    /**
     * User to instantiate a new instance of the FriendsDialogFragment class
     *
     * @param whichDialogToShow the type of dialog to display to the user
     * @param fragmentActivity  reference to calling(underlying activity)
     * @return new instance of this class
     */
    static FriendsDialogFragment newInstance(DialogToShow whichDialogToShow, FriendsFragment fragmentActivity) {
        FriendsDialogFragment newFragment = new FriendsDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable("whichDialog", whichDialogToShow);
        newFragment.setArguments(args);

        mFragmentActivity = fragmentActivity;

        return newFragment;


    }

    /**
     * Called when a dialog fragment is created
     *
     * @param savedInstanceState any passed data
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuthManager = AuthenticationManager.getInstance(mFragmentActivity.getContext());
        mWhichDialog = (DialogToShow) getArguments().getSerializable("whichDialog");
        sNegativeButtonText = BACK;
        sPositiveButtonText = INVITE_FRIEND_SHORT;
        mDialogTitle = FIND_FRIEND;

        /**
         * Depending on what stage of Invite Friends a user is on, and what option they have
         * selected, we change the dialog they are shown
         */
        switch (mWhichDialog) {

            //Standard dialog option containing all of the options
            case STANDARD_DIALOG:
                mDialogTitle = ADD_FRIEND_TITLE;
                mHeaderIcon = R.drawable.snaption_icon;
                sNegativeButtonText = CANCEL;
                sPositiveButtonText = INVITE_FRIEND_LONG;
                break;
            //User selected to invite friends via phone #
            case PHONE_INVITE:
                mHeaderIcon = R.drawable.ic_phone;
                sHint = mHints[0];
                break;
            //User selected to invite friends via Facebook
            case FACEBOOK_INVITE:
                mHeaderIcon = R.drawable.ic_facebook;
                sHint = mHints[1];
                break;
            //User selected to invite friends via email
            case EMAIL_INVITE:
                mHeaderIcon = R.drawable.ic_email;
                sHint = mHints[2];
                break;
        }

    }


    /**
     * Called when a dialog is created. Used to inflate any custom views and assign values
     *
     * @param savedInstanceState any saved data
     * @return newly created dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mDialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        //Inflate custom title
        View headerView = inflater.inflate(R.layout.dialog_header, null);

        //Inflate custom title text
        TextView headerText = (TextView) headerView.findViewById(R.id.dialogTitleText);

        //Inflate custom title icon
        ImageView headerIcon = (ImageView) headerView.findViewById(R.id.dialogTitleHeaderIcon);

        //Set the title bar
        headerText.setText(mDialogTitle);
        headerIcon.setImageResource(mHeaderIcon);

        //Assign Title
        mDialogBuilder.setCustomTitle(headerView);


        /**
         * Inflate a view that holds the
         */
        if (mWhichDialog == DialogToShow.STANDARD_DIALOG) {
            ListView friendList = (ListView) inflater.inflate(R.layout.custom_friend_dialog, null);
            mDialogBuilder.setView(friendList);
            friendList.setAdapter(new AddFriendsAdapter());
        }
        /**
         * Change the following for your own screen on the invite dialog
         */
        else {
            View view = inflater.inflate(R.layout.find_friend_layout, null);
            mDialogBuilder.setView(view);
            search = (EditText) view.findViewById(R.id.friendSearchView);

            /**
             * Determine what dialog is being shown.
             */
            if (mWhichDialog == DialogToShow.EMAIL_INVITE) {
                search.setInputType(TYPE_TEXT_VARIATION_EMAIL_ADDRESS);//Different type fo keyboard for emails
                search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                        /**
                         * Not totally sure what KeyEvent is the NEXT button on the keyboard.
                         * Will fix
                         */
                        if (i == 5) {

                            findFriend();
                            return true;
                        }
                        return false;
                    }
                });

                search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        /**
                         * If the text contains an email we should try and find a user
                         */
                        if (search.getText().toString().contains(".com") ||
                                search.getText().toString().contains(".net") ||
                                search.getText().toString().contains(".org")) {
                            findFriend();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {}
                });
            }


            //The following is a fancy listener for number inputs.
            //CURRENTLY WERE NOT USING THIS BECAUSE OF SOME REASON UNKNOWN TO ME
            /**
             search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            int len = search.getText().length();
            if(len == 1) {
            search.setText("(" + search.getText());
            search.setSelection(search.getText().length());

            }
            if (len == 4) {
            search.setText(search.getText() + ") ");
            search.setSelection(search.getText().length());
            }
            if (len == 9) {
            search.setText(search.getText() + "-");
            search.setSelection(search.getText().length());
            }
            if (len == 14)
            findFriend();
            }

            @Override public void afterTextChanged(Editable editable) {

            }
            });
             **/
            //hint is assigned above when the dialog to display is chosen
            search.setHint(sHint); //Sets the hint based off of which method
            // the user is adding a friend
            mResults = (RecyclerView) view.findViewById(R.id.search_results);
            mResults.setLayoutManager(new LinearLayoutManager(view.getContext()));
            ArrayList<Friend> friends = new ArrayList<>();
            if (mWhichDialog == DialogToShow.FACEBOOK_INVITE) {
                //provide friends
                loadFacebookFriends();
            }

            mAdapter = new FriendsAdapter(view.getContext(), friends);
            mResults.setAdapter(mAdapter);
        }


        /**
         * Onclick listeners and design for the Positive/Negative buttons at the bottom of a dialog.
         * These two buttons form their own section. On a standard dialog, the negative button will
         * close the dialog. On any of the other dialogs, the negative button will return to the
         * previous dialog.
         */
        mDialogBuilder.setPositiveButton(sPositiveButtonText, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Only send an outer app if we are still on the first dialog screen. Otherwise
                //we handle the friend invite in app
                if (mWhichDialog.equals(DialogToShow.STANDARD_DIALOG))
                    sendInviteIntent();
                else
                    addFriend();
            }
        }).setNegativeButton(sNegativeButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((FriendsFragment) mFragmentActivity).negativeButtonClicked(mWhichDialog);
            }
        });


        return mDialogBuilder.create();

    }

    private void sendInviteIntent() {
        String title = "Invite friend via";
        String smsBody = "Hey there download this nifty app called Snaption :^) \n" +
                "https://goo.gl/FWrtSX";

        Intent inviteIntent = new Intent();
        inviteIntent.setAction(Intent.ACTION_SEND);
        inviteIntent.putExtra(Intent.EXTRA_TEXT, smsBody);
        inviteIntent.setType("text/plain");

        Intent chooser = Intent.createChooser(inviteIntent, title);
        startActivity(chooser);
    }


    /**
     * Find a friend after a user has entered an email in the search bar
     */
    private void findFriend() {
        if (mWhichDialog == DialogToShow.PHONE_INVITE) {
            //Lol
        }
        //Using email to find a friend
        else {
            UserProvider.getUserWithEmail(search.getText().toString())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::showFriend,
                          Timber::e,
                          () -> Timber.i("Found user successfully"));
        }

    }


    /**
     * Update the recycler view with found friend
     *
     * @param user User returned from the database
     */
    private void showFriend(User user) {
        Friend tmpFriend = new Friend();
        List<Friend> friendList = new ArrayList<Friend>();
        tmpFriend.userName = user.username;
        tmpFriend.picture = user.picture;
        tmpFriend.email = search.getText().toString();

        sUserID = user.id;
        System.out.println(sUserID);
        System.out.println(mAuthManager.getSnaptionUserId());
        friendList.add(tmpFriend);
        mAdapter.setFriends(friendList);

    }

    /**
     * Add a friendId to our list of users
     */
    private void addFriend() {
        FriendProvider.addFriend(mAuthManager.getSnaptionUserId(), new AddFriendRequest(sUserID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(request -> {
                }, Timber::e, () -> Timber.i("Successfully added friend!"));
    }

    private void loadFacebookFriends() {
        FriendProvider.getFacebookFriends()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(friends -> mAdapter.setFriends(friends), Timber::e, () -> {
                });
    }

    /**
     * Inner class is used because it is really only important to the FriendsDialogFragment
     */
    private class AddFriendsAdapter extends BaseAdapter {

        /**
         * Container for the various options to invite a friend to snaption
         */
        private final String[] mInviteOptions = {"via Phone #", "via Facebook", "via Email"};


        /**
         * Container for the various icons to display with a selected invite option
         */
        private int[] mIconData = {R.drawable.ic_phone,
                R.drawable.ic_facebook,
                R.drawable.ic_email};

        /**
         * Empty constructor.
         */
        private AddFriendsAdapter() {
        }

        /**
         * Getter for the length of the friends adapter
         *
         * @return length of the data array
         */
        @Override
        public int getCount() {
            return mInviteOptions.length;
        }

        /**
         * Getter for an item at a specific index of the data array
         *
         * @param position index to get an item
         * @return item at the position index of the array
         */
        @Override
        public Object getItem(int position) {
            return mInviteOptions[position];
        }

        /**
         * Not used
         *
         * @param i index of the item
         * @return custom id of the item
         */
        @Override
        public long getItemId(int i) {
            return 0;
        }

        /**
         * Custom view inflater used to display each item of the Friends Adapter
         *
         * @param position    which item to display
         * @param convertView Parent View
         * @param viewGroup
         * @return newly created view
         */
        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view = convertView;

            /**
             * Determine if a view has already been created
             */
            if (view == null)
                view = getActivity().getLayoutInflater().inflate(R.layout.friend_invite_option_layout, null);


            TextView inviteText = (TextView) view.findViewById(R.id.inviteOptionText);
            inviteText.setText(mInviteOptions[position]);

            ImageView inviteImage = (ImageView) view.findViewById(R.id.inviteOptionIcon);
            inviteImage.setImageResource(mIconData[position]);

            /**
             * Important. This listener handles the user selecting an option from the invite friends
             * list. Once clicked we signal to the parent activity to inflate a new dialog.
             */
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
