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

import com.afollestad.materialdialogs.MaterialDialog;
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
    private FriendsContract.Presenter mPresenter;
    private Context mContext;

    private static final int AVATAR_SIZE = 40;
    private static final float DIM = .6F;
    private static final float BRIGHT = 1F;
    private boolean mShouldDisplayAddRemoveIcon;

    public FriendsAdapter(List<Friend> friends) {
        this.mFriends = friends;
        mSelectedIds = new ArrayList<>();
        mSelectedNames = new ArrayList<>();
        mSelectable = false;
    }

    public void setSelectable() {
        mSelectable = true;
    }

    public void setPresenter(FriendsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void setContextForDialog(Context context) {
        mContext = context;
    }

    public void setShouldDisplayAddRemoveOption(boolean should) {
        mShouldDisplayAddRemoveIcon = should;
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

        if (mShouldDisplayAddRemoveIcon) {
            if (curFriend.isSnaptionFriend) {
                holder.mAddRemoveFriendIcon.setImageResource(R.drawable.ic_remove_circle_outline_grey_800_24dp);
                holder.mAddRemoveFriendIcon.setOnClickListener(
                        swapViewsAndListeners(holder, true, holder.getAdapterPosition()));
            }
            else {
                holder.mAddRemoveFriendIcon.setImageResource(R.drawable.ic_person_add_grey_800_24dp);
                holder.mAddRemoveFriendIcon.setOnClickListener(
                        swapViewsAndListeners(holder, false, holder.getAdapterPosition()));
            }
        }

    }

    private View.OnClickListener swapViewsAndListeners(FriendViewHolder viewHolder, boolean addFriendView, int pos) {
        String finalFriendUsername = mFriends.get(pos).username;
        View.OnClickListener listener;
        if (addFriendView) {
            listener = view -> new MaterialDialog.Builder(mContext)
                .title(mContext.getString(R.string.remove_friend))
                .content(String.format(mContext.getString(R.string.remove_friend_body), finalFriendUsername))
                .positiveText(R.string.yes)
                .onPositive((materialDialog, dialogAction) -> {
                    viewHolder.mAddRemoveFriendIcon.setImageResource(R.drawable.ic_remove_circle_outline_grey_800_24dp);
                    mPresenter.removeFriend(mFriends.get(pos).id);
                    mPresenter.removeTempFriend(mFriends.get(pos).id);
                })
                .negativeText(R.string.no)
                .show();
        }
        else {
            listener = view -> new MaterialDialog.Builder(mContext)
                .title(mContext.getString(R.string.add_friend))
                .content(String.format(mContext.getString(R.string.add_friend_body), finalFriendUsername))
                .positiveText(R.string.yes)
                .onPositive((materialDialog, dialogAction) -> {
                    viewHolder.mAddRemoveFriendIcon.setImageResource(R.drawable.ic_person_add_grey_800_24dp);
                    mPresenter.addFriend(mFriends.get(pos).id);
                    mPresenter.addFriendTemp(mFriends.get(pos));
                })
                .negativeText(R.string.no)
                .show();
        }
        return listener;
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
            if (!friend.isSnaptionFriend) {
                this.mFriends.add(0, friend);
                notifyItemInserted(0);
            }
            else {
                this.mFriends.add(friend);
                notifyItemInserted(mFriends.size() - 1);
            }
        }
    }

    public void removeFriend(int position) {
        this.mFriends.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
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
