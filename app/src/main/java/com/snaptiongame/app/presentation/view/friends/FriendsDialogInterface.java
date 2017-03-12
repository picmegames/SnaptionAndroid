package com.snaptiongame.app.presentation.view.friends;

import com.snaptiongame.app.data.models.Friend;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nickromero on 3/10/17.
 */

public interface FriendsDialogInterface {

     void negativeButtonClicked(FriendsDialogFragment.DialogToShow whichDialog);

     void updateFriendsDialog(FriendsDialogFragment.DialogToShow dialogToShow);

     List<Friend> getFriends();
}
