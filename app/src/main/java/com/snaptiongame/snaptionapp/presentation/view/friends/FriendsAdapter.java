package com.snaptiongame.snaptionapp.presentation.view.friends;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
    private Context mContext;
    private List<Friend> mFriends;

    public FriendsAdapter(Context context, List<Friend> friends) {
        this.mContext = context;
        this.mFriends = friends;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.friend_card, parent, false);
        return new FriendViewHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        FriendViewHolder holder = (FriendViewHolder) viewHolder;
        Friend curFriend = mFriends.get(position);

        holder.mName.setText(curFriend.fullName);
        holder.mUserName.setText(curFriend.userName);
        if (curFriend.picture != null && !curFriend.picture.isEmpty()) {
            Glide.with(mContext)
                    .load(curFriend.picture)
                    .into(holder.mImage);
        }
        else if (curFriend.lastName != null) {
            holder.mImage.setImageDrawable(TextDrawable.builder()
                    .beginConfig()
                    .width(40)
                    .height(40)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(curFriend.firstName.substring(0, 1) + curFriend.lastName
                            .substring(0, 1),
                            ColorGenerator.MATERIAL.getRandomColor()));
        }
    }

    public void setFriends(List<Friend> friends) {
        this.mFriends = friends;
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
