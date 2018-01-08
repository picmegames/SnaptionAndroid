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
    private boolean showExp;
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
                presenter.removeFriend(name, this.friends.get(position).getId());
            }
            else {
                presenter.addFriend(name, this.friends.get(position).getId());
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
                profileIntent.putExtra(User.USERNAME, curFriend.getUsername());
                profileIntent.putExtra(User.IMAGE_URL, curFriend.getImageUrl());
                profileIntent.putExtra(User.ID, curFriend.getId());

                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                        .makeSceneTransitionAnimation((AppCompatActivity) context, view,
                                ViewCompat.getTransitionName(view));
                context.startActivity(profileIntent, transitionActivityOptions.toBundle());
            });
        }
        else {
            if (selectedIds.contains(curFriend.getId())) {
                holder.itemView.setAlpha(BRIGHT);
            }
            else {
                holder.itemView.setAlpha(DIM);
            }

            holder.itemView.setOnClickListener(view -> {
                if (!selectedIds.contains(curFriend.getId())) {
                    view.setAlpha(BRIGHT);
                    selectedIds.add(curFriend.getId());
                    selectedNames.add(curFriend.getUsername());
                }
                else {
                    view.setAlpha(DIM);
                    selectedIds.remove(Integer.valueOf(curFriend.getId()));
                    selectedNames.remove(curFriend.getUsername());
                }
            });
        }

        if (!curFriend.getFullName().isEmpty()) {
            holder.name.setText(curFriend.getFullName());
        }
        else {
            holder.name.setText(curFriend.getUsername());
        }

        holder.usernameField.setText(curFriend.getUsername());
        if (curFriend.getImageUrl() != null && !curFriend.getImageUrl().isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .placeholder(new ColorDrawable(ContextCompat.getColor(holder.context, R.color.grey_300)));

            Glide.with(holder.context)
                    .load(curFriend.getImageUrl())
                    .apply(options)
                    .into(holder.image);
        }
        else {
            holder.image.setImageDrawable(TextDrawable.builder()
                    .beginConfig()
                    .width(AVATAR_SIZE)
                    .height(AVATAR_SIZE)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(curFriend.getUsername().substring(0, 1),
                            ColorGenerator.MATERIAL.getColor(curFriend.getUsername())));
        }

        holder.friendName = curFriend.getUsername();
        holder.isCurrentUser = curFriend.getId() == AuthManager.getUserId();

        if (shouldDisplayAddRemoveIcon && !holder.isCurrentUser) {
            holder.addRemoveFriendIcon.setVisibility(View.VISIBLE);
            holder.setAddRemoveFriendIcon(holder.isSnaptionFriend = curFriend.isSnaptionFriend());
        }
        else {
            holder.addRemoveFriendIcon.setVisibility(View.GONE);
        }

        if (showExp) {
            holder.exp.setVisibility(View.VISIBLE);
            holder.exp.setText(String.valueOf(curFriend.getExp()));

            if (curFriend.getUsername().equals(AuthManager.getUsername())) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.context, R.color.colorAccent));
                holder.itemView.getBackground().setAlpha(75);
            }
            else {
                holder.itemView.setBackground(null);
            }
        }
        else {
            holder.exp.setVisibility(View.GONE);
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

    public void setShowExp(boolean showExp) {
        this.showExp = showExp;
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
            selectedNames.add(getFriendById(friendId).getUsername());
            notifyItemChanged(friends.indexOf(getFriendById(friendId)));
        }
    }

    public void deselectFriend(int friendId) {
        if (selectedIds.contains(friendId)) {
            selectedIds.remove(Integer.valueOf(friendId));
            selectedNames.remove(getFriendById(friendId).getUsername());
            notifyItemChanged(friends.indexOf(getFriendById(friendId)));
        }
    }

    public void setCallback(FriendItemListener callback) {
        this.callback = callback;
    }

    public Friend getFriendById(int friendId) {
        for (Friend friend : friends) {
            if (friend.getId() == friendId) {
                return friend;
            }
        }

        return null;
    }

    public Friend getFriendByName(String name) {
        for (Friend friend : friends) {
            if (friend.getUsername().equals(name)) {
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
            if (!friend.isSnaptionFriend()) {
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
