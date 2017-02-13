package com.snaptiongame.snaptionapp.data.models;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Brian Gouldsberry
 */
@Entity(indexes = {
      @Index(value = "id", unique = true)
})
public class Friend {
    @Id
    public String id;
    public String firstName;
    public String lastName;
    public String fullName;
    @SerializedName("username")
    public String userName;
    public String picture;
    public String cover;
    public String email;
    public boolean isSnaptionFriend;

    public Friend() {

    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof  Friend) {
            Friend other = (Friend) o;
            return (userName.equals(other.userName));
        }
        return false;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCover() {
        return this.cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getIsSnaptionFriend() {
        return this.isSnaptionFriend;
    }

    public void setIsSnaptionFriend(boolean isSnaptionFriend) {
        this.isSnaptionFriend = isSnaptionFriend;
    }

    public Friend(String id, String first, String last, String fullName, String userName, String picture,
                  String cover, String email) {
        this.id = id;
        this.firstName = first;
        this.lastName = last;
        this.fullName = fullName;
        this.userName = userName;
        this.picture = picture;
        this.cover = cover;
        this.email = email;
        isSnaptionFriend = false;
    }

    @Generated(hash = 104765485)
    public Friend(String id, String firstName, String lastName, String fullName, String userName, String picture,
            String cover, String email, boolean isSnaptionFriend) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.userName = userName;
        this.picture = picture;
        this.cover = cover;
        this.email = email;
        this.isSnaptionFriend = isSnaptionFriend;
    }
}
