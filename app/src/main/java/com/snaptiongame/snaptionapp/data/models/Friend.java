package com.snaptiongame.snaptionapp.data.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Brian Gouldsberry
 */

public class Friend extends RealmObject {
    @PrimaryKey
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
            return (id.equals(other.id) && firstName.equals(other.fullName) && lastName.equals(other
                    .lastName) && fullName.equals(other.fullName) && userName.equals(other.userName));
        }
        return false;
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
}
