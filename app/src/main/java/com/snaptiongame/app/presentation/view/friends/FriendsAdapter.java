package com.snaptiongame.app.presentation.view.friends;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.auth.AuthManager;
import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.presentation.view.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Brian Gouldsberry
 */

public class FriendsAdapter extends RecyclerView.Adapter {
    private List<Friend> friends;
    private List<Integer> selectedIds;
    private List<String> selectedNames;
    private FriendsContract.Presenter presenter;
    private FriendItemListener callback;
    private boolean selectable;
    private int lastPosition = -1;
    private boolean shouldDisplayAddRemoveIcon;

    private static final int AVATAR_SIZE = 40;
    private static final float DIM = .6F;
    private static final float BRIGHT = 1F;

    public FriendsAdapter(List<Friend> friends) {
        this.friends = friends;
        selectedIds = new ArrayList<>();
        selectedNames = new ArrayList<>();
        selectable = false;

        callback = (name, isAdded, position) -> {
            if (isAdded) {
                presenter.removeFriend(name, this.friends.get(position).id);
            }
            else {
                presenter.addFriend(name, this.friends.get(position).id);
            }
        };
    }

    public void setSelectable() {
        selectable = true;
    }

    public void setPresenter(FriendsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public void setShouldDisplayAddRemoveOption(boolean should) {
        shouldDisplayAddRemoveIcon = should;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_card, parent, false);
        return new FriendViewHolder(view, callback);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        FriendViewHolder holder = (FriendViewHolder) viewHolder;
        Friend curFriend = friends.get(position);

        if (!selectable) {
            Context context = holder.itemView.getContext();
            holder.image.setOnClickListener(view -> {
                Intent profileIntent = new Intent(context, ProfileActivity.class);
                profileIntent.putExtra(ProfileActivity.IS_CURRENT_USER, false);
                profileIntent.putExtra(User.USERNAME, curFriend.username);
                profileIntent.putExtra(User.IMAGE_URL, curFriend.imageUrl);
                profileIntent.putExtra(User.ID, curFriend.id);
                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                        .makeSceneTransitionAnimation((AppCompatActivity) context, holder.image,
                                ViewCompat.getTransitionName(holder.image));
                context.startActivity(profileIntent, transitionActivityOptions.toBundle());
            });
        }
        else {
            if (selectedIds.contains(curFriend.id)) {
                holder.itemView.setAlpha(BRIGHT);
            }
            else {
                holder.itemView.setAlpha(DIM);
            }

            holder.itemView.setOnClickListener(view -> {
                if (!selectedIds.contains(curFriend.id)) {
                    view.setAlpha(BRIGHT);
                    selectedIds.add(curFriend.id);
                    selectedNames.add(curFriend.username);
                }
                else {
                    view.setAlpha(DIM);
                    selectedIds.remove(Integer.valueOf(curFriend.id));
                    selectedNames.remove(curFriend.username);
                }
            });
        }

        if (curFriend.fullName != null && !curFriend.fullName.isEmpty()) {
            holder.name.setText(curFriend.fullName);
        }
        else {
            holder.name.setText(curFriend.username);
        }

        holder.usernameField.setText(curFriend.username);
        if (curFriend.imageUrl != null && !curFriend.imageUrl.isEmpty()) {

            RequestOptions options = new RequestOptions()
                    .placeholder(new ColorDrawable(ContextCompat.getColor(holder.context, R.color.grey_300)))
                    .dontAnimate();

            Glide.with(holder.context)
                    .load(curFriend.imageUrl)
                    .apply(options)
                    .into(holder.image);
        }
        else if (curFriend.imageUrl == null) {
            holder.image.setImageDrawable(TextDrawable.builder()
                    .beginConfig()
                    .width(AVATAR_SIZE)
                    .height(AVATAR_SIZE)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(curFriend.username.substring(0, 1),
                            ColorGenerator.MATERIAL.getColor(curFriend.username)));
        }

        holder.friendName = curFriend.username;
        holder.isCurrentUser = curFriend.id == AuthManager.getUserId();

        if (shouldDisplayAddRemoveIcon && !holder.isCurrentUser) {
            holder.addRemoveFriendIcon.setVisibility(View.VISIBLE);
            holder.setAddRemoveFriendIcon(holder.isSnaptionFriend = curFriend.isSnaptionFriend);
        }
        else {
            holder.addRemoveFriendIcon.setVisibility(View.GONE);
        }

        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.fade_in);
            view.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        ((FriendViewHolder) holder).itemView.clearAnimation();
    }

    public void setFriends(List<Friend> friends) {
        if (!this.friends.equals(friends)) {
            this.friends = friends;
            notifyDataSetChanged();
        }
    }

    public void addFriends(List<Friend> friends) {
        int oldSize = this.friends.size();
        this.friends.addAll(friends);
        notifyItemRangeInserted(oldSize, this.friends.size());
    }

    public void selectFriend(int friendId) {
        if (!selectedIds.contains(friendId)) {
            selectedIds.add(friendId);
            selectedNames.add(getFriendById(friendId).username);
            notifyItemChanged(friends.indexOf(getFriendById(friendId)));
        }
    }

    public void deselectFriend(int friendId) {
        if (selectedIds.contains(friendId)) {
            selectedIds.remove(Integer.valueOf(friendId));
            selectedNames.remove(getFriendById(friendId).username);
            notifyItemChanged(friends.indexOf(getFriendById(friendId)));
        }
    }

    public void setCallback(FriendItemListener callback) {
        this.callback = callback;
    }

    public Friend getFriendById(int friendId) {
        for (Friend friend : friends) {
            if (friend.id == friendId) {
                return friend;
            }
        }

        return null;
    }

    public Friend getFriendByName(String name) {
        for (Friend friend : friends) {
            if (friend.username.equals(name)) {
                return friend;
            }
        }

        return null;
    }

    public boolean isEmpty() {
        return friends.isEmpty();
    }

    public void addFriend(Friend friend) {
        // Ensures that users who are not your friend appear at the top of the list
        if (!friends.contains(friend)) {
            if (!friend.isSnaptionFriend) {
                friends.add(0, friend);
                notifyItemInserted(0);
            }
            else {
                friends.add(friend);
                notifyItemInserted(friends.size() - 1);
            }
        }
    }

    public List<String> getSelectedFriendNames() {
        return selectedNames;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void clear() {
        lastPosition = -1;
        int oldSize = friends.size();
        friends.clear();
        notifyItemRangeRemoved(0, oldSize);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }
}
