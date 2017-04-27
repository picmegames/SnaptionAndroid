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
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.presentation.view.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Brian Gouldsberry
 */

public class FriendsAdapter extends RecyclerView.Adapter {
    private List<Friend> mFriends;
    private List<Integer> mSelectedIds;
    private List<String> mSelectedNames;
    private boolean mSelectable;
    private int lastPosition = -1;
    private FriendsContract.Presenter mPresenter;

    private static final int AVATAR_SIZE = 40;
    private static final float DIM = .6F;
    private static final float BRIGHT = 1F;
    private static final int EMPTY_VIEW = 10;

    public FriendsAdapter(List<Friend> friends, FriendsContract.Presenter presenter) {
        this.mFriends = friends;
        mSelectedIds = new ArrayList<>();
        mSelectedNames = new ArrayList<>();
        mSelectable = false;
        mPresenter = presenter;
    }

    public void setSelectable() {
        mSelectable = true;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_card, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        FriendViewHolder holder = (FriendViewHolder) viewHolder;
        Friend curFriend = mFriends.get(position);

        if (!mSelectable) {
            Context context = holder.itemView.getContext();
            holder.itemView.setOnClickListener(view -> {
                Intent profileIntent = new Intent(context, ProfileActivity.class);
                profileIntent.putExtra(ProfileActivity.IS_CURRENT_USER, false);
                profileIntent.putExtra(User.USERNAME, curFriend.username);
                profileIntent.putExtra(User.IMAGE_URL, curFriend.imageUrl);
                profileIntent.putExtra(User.ID, curFriend.id);
                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                        .makeSceneTransitionAnimation((AppCompatActivity) context, holder.mImage,
                                ViewCompat.getTransitionName(holder.mImage));
                context.startActivity(profileIntent, transitionActivityOptions.toBundle());
            });
        }
        else {
            holder.itemView.setAlpha(DIM);

            holder.itemView.setOnClickListener(view -> {
                if (!mSelectedIds.contains(curFriend.id)) {
                    view.setAlpha(BRIGHT);
                    mSelectedIds.add(curFriend.id);
                    mSelectedNames.add(curFriend.username);
                }
                else {
                    view.setAlpha(DIM);
                    mSelectedIds.remove(Integer.valueOf(curFriend.id));
                    mSelectedNames.remove(curFriend.username);
                }
            });
        }

        holder.mName.setText(curFriend.fullName);
        holder.mUsernameField.setText(curFriend.username);
        if (curFriend.imageUrl != null && !curFriend.imageUrl.isEmpty()) {
            Glide.with(holder.mContext)
                    .load(curFriend.imageUrl)
                    .placeholder(new ColorDrawable(ContextCompat.getColor(holder.mContext, R.color.grey_300)))
                    .dontAnimate()
                    .into(holder.mImage);
        }
        else if (curFriend.imageUrl == null) {
            holder.mImage.setImageDrawable(TextDrawable.builder()
                    .beginConfig()
                    .width(AVATAR_SIZE)
                    .height(AVATAR_SIZE)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(curFriend.username.substring(0, 1),
                            ColorGenerator.MATERIAL.getColor(curFriend.username)));
        }
/*
        if (curFriend.isSnaptionFriend) {
            holder.add_remove_friend_icon.setImageResource(R.drawable.ic_remove_circle_outline_grey_800_24dp);
            holder.add_remove_friend_icon.setOnClickListener(v -> {
                holder.add_remove_friend_icon.setImageResource(R.drawable.ic_person_add_grey_800_24dp);
                mPresenter.removeFriend(mFriends.get(position).id);
                removeFriend(position);
            });
        }
        else {
            holder.add_remove_friend_icon.setImageResource(R.drawable.ic_person_add_grey_800_24dp);
            holder.add_remove_friend_icon.setOnClickListener(v -> {
                holder.add_remove_friend_icon.setImageResource(R.drawable.ic_remove_circle_outline_grey_800_24dp);
                mPresenter.addFriend(mFriends.get(position).id);
                addFriend(mFriends.get(position));
            });
        }*/
    }

    private View.OnClickListener swapViewsAndListeners(ImageView viewHolder, boolean addFriendView, int lastPosition) {
        if (addFriendView) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.setImageResource(R.drawable.ic_remove_circle_outline_grey_800_24dp);
                    mPresenter.removeFriend(mFriends.get(lastPosition).id);
                    removeFriend(lastPosition);
                }
            };
        }
        else {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.setImageResource(R.drawable.ic_person_add_grey_800_24dp);
                    mPresenter.addFriend(mFriends.get(lastPosition).id);
                    addFriend(mFriends.get(lastPosition));
                }
            };
        }

    }

    public void setFriends(List<Friend> friends) {
        if (!friends.equals(mFriends)) {
            mFriends = friends;
        }
        notifyDataSetChanged();
    }

    public void addFriend(Friend friend) {

        //Ensures that users who are not your friend appear at the top of the list
        if (!this.mFriends.contains(friend)) {
            if (!friend.isSnaptionFriend)
                this.mFriends.add(0, friend);
            else
                this.mFriends.add(friend);
            notifyDataSetChanged();
        }

    }

    public void removeFriend (int position) {
        this.mFriends.remove(position);
        notifyItemRemoved(position);
    }

    public void selectFriend(int position) {
        this.mSelectedIds.add(mFriends.get(position).id);
    }

    public void deselectFriend(int position) {
        this.mSelectedIds.remove(position);
    }

    public boolean isSelected(int position) {
        return mSelectedIds.contains(mFriends.get(position).id);
    }

    public List<Integer> getSelectedFriendIds() {
        return mSelectedIds;
    }

    public List<String> getSelectedFriendNames() {
        return mSelectedNames;
    }

    public List<Friend> getFriends() {
        return mFriends;
    }

    public void clearFriends() {
        mFriends.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
