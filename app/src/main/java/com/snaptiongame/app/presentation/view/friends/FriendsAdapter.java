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
    private List<Friend> mFriends;
    private List<Integer> mSelectedIds;
    private List<String> mSelectedNames;
    private FriendsContract.Presenter mPresenter;
    private FriendItemListener mCallback;
    private boolean mSelectable;
    private int lastPosition = -1;

    private static final int AVATAR_SIZE = 40;
    private static final float DIM = .6F;
    private static final float BRIGHT = 1F;
    private boolean mShouldDisplayAddRemoveIcon;

    public FriendsAdapter(List<Friend> friends) {
        this.mFriends = friends;
        mSelectedIds = new ArrayList<>();
        mSelectedNames = new ArrayList<>();
        mSelectable = false;

        mCallback = (name, isAdded, position) -> {
            if (isAdded) {
                mPresenter.removeFriend(name, mFriends.get(position).id);
            }
            else {
                mPresenter.addFriend(name, mFriends.get(position).id);
            }
        };
    }

    public void setSelectable() {
        mSelectable = true;
    }

    public void setPresenter(FriendsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void setShouldDisplayAddRemoveOption(boolean should) {
        mShouldDisplayAddRemoveIcon = should;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_card, parent, false);
        return new FriendViewHolder(view, mCallback);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        FriendViewHolder holder = (FriendViewHolder) viewHolder;
        Friend curFriend = mFriends.get(position);

        if (!mSelectable) {
            Context context = holder.itemView.getContext();
            holder.mImage.setOnClickListener(view -> {
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

        if (curFriend.fullName != null && !curFriend.fullName.isEmpty()) {
            holder.mName.setText(curFriend.fullName);
        }
        else {
            holder.mName.setText(curFriend.username);
        }

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

        holder.friendName = curFriend.username;
        holder.isCurrentUser = curFriend.id == AuthManager.getUserId();

        if (mShouldDisplayAddRemoveIcon && !holder.isCurrentUser) {
            holder.mAddRemoveFriendIcon.setVisibility(View.VISIBLE);
            holder.setAddRemoveFriendIcon(holder.isSnaptionFriend = curFriend.isSnaptionFriend);
        }
        else {
            holder.mAddRemoveFriendIcon.setVisibility(View.GONE);
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
        mFriends = friends;
        notifyDataSetChanged();
    }

    public void addFriends(List<Friend> friends) {
        int oldSize = mFriends.size();
        mFriends.addAll(friends);
        notifyItemRangeInserted(oldSize, mFriends.size());
    }

    public boolean isEmpty() {
        return mFriends.isEmpty();
    }

    public void addFriend(Friend friend) {
        // Ensures that users who are not your friend appear at the top of the list
        if (!mFriends.contains(friend)) {
            if (!friend.isSnaptionFriend) {
                mFriends.add(0, friend);
                notifyItemInserted(0);
            }
            else {
                mFriends.add(friend);
                notifyItemInserted(mFriends.size() - 1);
            }
        }
    }

    public List<String> getSelectedFriendNames() {
        return mSelectedNames;
    }

    public List<Friend> getFriends() {
        return mFriends;
    }

    public void clear() {
        int oldSize = mFriends.size();
        mFriends.clear();
        notifyItemRangeRemoved(0, oldSize);
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
