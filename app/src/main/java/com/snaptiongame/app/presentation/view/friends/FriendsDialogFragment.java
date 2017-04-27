package com.snaptiongame.app.presentation.view.friends;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.AddFriendRequest;
import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.data.providers.FriendProvider;
import com.snaptiongame.app.data.providers.UserProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
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

    private static final int INVITE_TO_SNAPTION_POSITION = 3;
    private AddFriendsAdapter mAddFriendsAdapter;

    /**
     * A representation of what dialog to show. Enum is used for readability.
     */
    public enum DialogToShow {
        PHONE_INVITE(0), FACEBOOK_INVITE(0), EMAIL_INVITE(1),
        STANDARD_DIALOG(2);

        private final int position;

        DialogToShow(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }
    }

    /**
     * Collection of Dialog Enums used for mapping a user's click
     */
    private DialogToShow[] mDialogOptions = {DialogToShow.PHONE_INVITE,
            DialogToShow.FACEBOOK_INVITE,
            DialogToShow.EMAIL_INVITE};

    /**
     * Holder for the type of dialog to show a user
     */
    private DialogToShow mWhichDialog;

    /**
     * List of the facebook friends that the user has to invite
     */
    private List<Friend> mFacebookFriends = new ArrayList<>();

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
     * Reference to the search view that is shown on the second dialog. Pulled out of local scope
     * from within the dialog to show option. This way we can access the field in any dialof or method.
     */
    private EditText search;

    /**
     * Member variables used to create the two buttons at the bottom of a dialog
     */
    private String sNegativeButtonText;
    private String sPositiveButtonText;

    private final int NEGATIVE_BUTTON_RESULT_CODE = 1;
    private int INVITE_OPTION_SELECT_CODE = 2;

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


    private TextView mEmpty;

    private ListView mFriendList;

    /**
     * Empty constructor for the dialog. Expected from a DialogFragment
     */
    public FriendsDialogFragment() {
    }


    /**
     * User to instantiate a new instance of the FriendsDialogFragment class
     *
     * @param whichDialogToShow the type of dialog to display to the user
     * @return new instance of this class
     */
    public static FriendsDialogFragment newInstance(DialogToShow whichDialogToShow) {
        FriendsDialogFragment newFragment = new FriendsDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable("whichDialog", whichDialogToShow);

        newFragment.setArguments(args);

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

        mWhichDialog = (DialogToShow) getArguments().getSerializable("whichDialog");

        sNegativeButtonText = getString(R.string.back);
        sPositiveButtonText = getString(R.string.add_friend);
        mDialogTitle = getString(R.string.find_friends);

        /**
         * Depending on what stage of Invite Friends a user is on, and what option they have
         * selected, we change the dialog they are shown
         */
        if (mWhichDialog != null) {
            switch (mWhichDialog) {

                //Standard dialog option containing all of the options
                case STANDARD_DIALOG:
                    mDialogTitle = getString(R.string.add_a_friend);
                    mHeaderIcon = R.drawable.snaption_icon;
                    sPositiveButtonText = "";
                    sNegativeButtonText = getString(R.string.cancel);
                    break;
                //User selected to invite friends via phone #
                case PHONE_INVITE:
                    mHeaderIcon = R.drawable.ic_phone;
                    sHint = getString(R.string.phone_hint);
                    break;
                //User selected to invite friends via Facebook
                case FACEBOOK_INVITE:
                    mHeaderIcon = R.drawable.ic_facebook;
                    sHint = getString(R.string.name_hint);
                    break;
                //User selected to invite friends via email
                case EMAIL_INVITE:
                    mHeaderIcon = R.drawable.ic_email;
                    sHint = getString(R.string.email_hint);
                    break;
            }
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
            mFriendList = (ListView) inflater.inflate(R.layout.custom_friend_dialog, null);
            mDialogBuilder.setView(mFriendList);
            mAddFriendsAdapter = new AddFriendsAdapter();
            mFriendList.setAdapter(mAddFriendsAdapter);
        }
        /**
         * Change the following for your own screen on the invite dialog
         */
        else {
            View view = inflater.inflate(R.layout.find_friend_layout, null);

            mDialogBuilder.setView(view);
            search = (EditText) view.findViewById(R.id.friendSearchView);
            mEmpty = (TextView) view.findViewById(R.id.empty_view);

            /**
             * Determine what dialog is being shown.
             */
            if (mWhichDialog == DialogToShow.EMAIL_INVITE) {

                search.setInputType(TYPE_TEXT_VARIATION_EMAIL_ADDRESS);//Different type fo keyboard for emails
                search.setOnEditorActionListener((textView, i, keyEvent) -> {

                    /**
                     * Not totally sure what KeyEvent is the NEXT button on the keyboard.
                     * Will fix
                     */
                    if (i == EditorInfo.IME_ACTION_SEND) {

                        findFriend();
                    }
                    return true;
                });


            } else if (mWhichDialog == DialogToShow.FACEBOOK_INVITE) {

                search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                        List<Friend> results = FriendsFragment.filterList(mFacebookFriends,
//                                search.getText().toString());
//                        mAdapter.setFriends(results);
//                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });
            }

            //hint is assigned above when the dialog to display is chosen
            search.setHint(sHint); //Sets the hint based off of which method
            // the user is adding a friend
            mResults = ButterKnife.findById(view, R.id.search_results);
            mResults.setLayoutManager(new LinearLayoutManager(view.getContext()));
            ArrayList<Friend> friends = new ArrayList<>();
           // mAdapter = new FriendsAdapter(friends);
            mAdapter.setSelectable();
            mResults.setAdapter(mAdapter);

            if (mWhichDialog == DialogToShow.FACEBOOK_INVITE) {
                mAdapter.setSelectable();
                mAdapter.setFriends(mFacebookFriends);
                loadFacebookFriends();
            }
        }


        /**
         * Onclick listeners and design for the Positive/Negative buttons at the bottom of a dialog.
         * These two buttons form their own section. On a standard dialog, the negative button will
         * close the dialog. On any of the other dialogs, the negative button will return to the
         * previous dialog.
         */
        mDialogBuilder.setPositiveButton(sPositiveButtonText, (dialogInterface, i) -> {
            //add all selected friends if in the facebook dialog
            if (mWhichDialog.equals(DialogToShow.FACEBOOK_INVITE)) {
                for (Integer friendId : mAdapter.getSelectedFriendIds()) {
                    addFriend(friendId);
                }
            }

            //Only send an outer app if we are still on the first dialog screen. Otherwise
            //we handle the friend invite in app
            else if (!mWhichDialog.equals(DialogToShow.STANDARD_DIALOG)) {
                addFriend(sUserID);
            }
        }).setNegativeButton(sNegativeButtonText, (dialogInterface, i) -> {

            Intent data = new Intent();
            data.putExtra("which", mWhichDialog);

            getTargetFragment().onActivityResult(0, NEGATIVE_BUTTON_RESULT_CODE, data);

        });

        return mDialogBuilder.create();

    }

    private void sendInviteIntent() {

        String smsBody = getString(R.string.invite_message) +
                getString(R.string.store_url);

        Intent inviteIntent = new Intent(Intent.ACTION_SEND);
        inviteIntent.putExtra(Intent.EXTRA_TEXT, smsBody);
        inviteIntent.setType("text/plain");

        Intent chooser = Intent.createChooser(inviteIntent, getString(R.string.invite_friend_via));

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
                    .subscribe(
                            this::showFriend,
                            Timber::e
                    );
        }

    }


    /**
     * Update the recycler view with found friend
     *
     * @param user User returned from the database
     */
    private void showFriend(User user) {
        //If the user exists then adapt them into a friend and dislpay them
        if (user.username != null) {
            Friend tmpFriend = new Friend();
            List<Friend> friendList = new ArrayList<>();
            tmpFriend.username = user.username;
            tmpFriend.imageUrl = user.imageUrl;
            tmpFriend.email = search.getText().toString();

            sUserID = user.id;

            friendList.add(tmpFriend);
            mAdapter.setFriends(friendList);
            mAdapter.notifyDataSetChanged();
            mEmpty.setVisibility(View.GONE);
        }
        //Display the no friends found dialog if not
        else {
            mAdapter.setFriends(new ArrayList<>());
            mAdapter.notifyDataSetChanged();
            mEmpty.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Add a friendId to our list of users
     */
    private void addFriend(int userId) {
        FriendProvider.addFriend(new AddFriendRequest(userId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        request -> updateFriendFragment(),
                        Timber::e
                );
    }

    /**
     * Gets the users list of facebook friends from the friend provider and then removes any that
     * overlap with existing snaption friends. The filtered list is then displayed in the view
     */
    private void loadFacebookFriends() {
        FriendProvider.getFacebookFriends()
                .filter(friends -> {
                    //This will break NOT ANYMORE
                    //friends.removeAll(mFriendsDialogInterface.getFriends());
                    return true;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        friends -> {

                            mAdapter.setFriends(friends);
                            mAdapter.notifyDataSetChanged();
                            if (friends.size() > 0) {
                                mEmpty.setVisibility(View.GONE);
                            } else {
                                mEmpty.setVisibility(View.VISIBLE);
                            }
                        },
                        Timber::e,
                        () -> Timber.i("Successfully loaded Facebook friends!")
                );
    }

    /**
     * Inner class is used because it is really only important to the FriendsDialogFragment
     */
    private class AddFriendsAdapter extends BaseAdapter {

        /**
         * Container for the various options to invite a friend to snaption
         */
        private final String[] mInviteOptions = {"Add via Phone #",
                "Add via Facebook", "Add via Email", "Invite to Game!"};

        private final int PHONE_POSITION = 0;
        private final String COMING_SOON_MESSAGE = "Coming Soon!";


        /**
         * Container for the various icons to display with a selected invite option
         */
        private int[] mIconData = {R.drawable.ic_phone,
                R.drawable.ic_facebook,
                R.drawable.ic_email,
                R.drawable.snaption_icon};


        /**
         * Empty constructor.
         */
        private AddFriendsAdapter(){};


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
            view.setOnClickListener(view1 -> {
                if (position == INVITE_TO_SNAPTION_POSITION)
                    sendInviteIntent();
                else if (position == PHONE_POSITION) {
                    Toast.makeText(getActivity(), COMING_SOON_MESSAGE, Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent data = new Intent();
                    data.putExtra("which", mDialogOptions[position]);
                    getTargetFragment().onActivityResult(0, INVITE_OPTION_SELECT_CODE, data);
                }
            });

            return view;
        }
    }

    public void updateFriendFragment() {
        FriendsFragment frag = (FriendsFragment) this.getTargetFragment();
        frag.mPresenter.loadFriends();
    }

}
