package com.snaptiongame.snaptionapp.presentation.view.friends;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.models.Friend;

import java.util.List;

/**
 * @author Brian Gouldsberry
 */

public class FriendsAdapter extends RecyclerView.Adapter {
    private List<Friend> mFriends;

    public FriendsAdapter(List<Friend> friends) {
        this.mFriends = friends;
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

        holder.mName.setText(curFriend.fullName);
        holder.mUserName.setText(curFriend.userName);
        if (curFriend.picture != null && !curFriend.picture.isEmpty()) {
            Glide.with(holder.mContext)
                    .load(curFriend.picture)
                    .into(holder.mImage);
        }
        else if (curFriend.picture == null) {
            holder.mImage.setImageDrawable(TextDrawable.builder()
                    .beginConfig()
                    .width(40)
                    .height(40)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(curFriend.userName.substring(0, 1),
                            ColorGenerator.MATERIAL.getRandomColor()));
        }
    }

    public void setFriends(List<Friend> friends) {

        this.mFriends = friends;
        notifyDataSetChanged();
    }

    public void clearFriends() {mFriends.clear();}


    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
