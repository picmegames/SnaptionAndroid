package com.snaptiongame.snaptionapp.presentation.view.friends;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.models.Friend;
import com.snaptiongame.snaptionapp.data.models.User;
import com.snaptiongame.snaptionapp.presentation.view.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Brian Gouldsberry
 */

public class FriendsAdapter extends RecyclerView.Adapter {
    private List<Friend> mFriends;
    private List<Integer> mSelected;
    private boolean mSelectable;
    public static final float DIM = .6F, BRIGHT = 1F;

    public FriendsAdapter(List<Friend> friends) {
        this.mFriends = friends;
        mSelected = new ArrayList<>();
        mSelectable = false;
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
                profileIntent.putExtra(User.USERNAME, curFriend.userName);
                profileIntent.putExtra(User.PICTURE, curFriend.picture);
                profileIntent.putExtra(User.ID, curFriend.id);
                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                        .makeSceneTransitionAnimation((AppCompatActivity) context, holder.mImage,
                                context.getString(R.string.shared_transition));
                context.startActivity(profileIntent, transitionActivityOptions.toBundle());
            });
        }
        else {
            holder.itemView.setAlpha(DIM);

            holder.itemView.setOnClickListener(view -> {
                if (!mSelected.contains(curFriend.id)) {
                    view.setAlpha(BRIGHT);
                    mSelected.add(curFriend.id);
                }
                else {
                    view.setAlpha(DIM);
                    mSelected.remove(Integer.valueOf(curFriend.id));
                }
            });
        }

        holder.mName.setText(curFriend.fullName);
        holder.mUsernameField.setText(curFriend.userName);
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
                            ColorGenerator.MATERIAL.getColor(curFriend.userName)));
        }
    }

    public void setFriends(List<Friend> friends) {
        this.mFriends = friends;
    }

    public void addFriend(Friend friend) {
        this.mFriends.add(friend);
    }

    public void selectFriend(int position) {
        this.mSelected.add(mFriends.get(position).id);
    }

    public void deselectFriend(int position) {
        this.mSelected.remove(position);
    }

    public boolean isSelected(int position) {
        return mSelected.contains(mFriends.get(position).id);
    }

    public List<Integer> getSelectedFriends() {
        return mSelected;
    }

    public List<Friend> getFriends() {
        return mFriends;
    }

    public void clearFriends() {
        mFriends.clear();
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
