package com.snaptiongame.snaptionapp.data.models;

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
    public String userName;
    public String picture;
    public String cover;
    public String email;

    public Friend() {

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
    }
}
