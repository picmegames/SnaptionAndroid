package com.snaptiongame.snaptionapp.data.models;

import io.realm.RealmObject;

/**
 * @author Brian Gouldsberry
 */

public class Friend extends RealmObject {
    public String id;
    public String firstName;
    public String lastName;
    public String fullName;
    public String picture;
    public String cover;
    public String email;

    public Friend() {

    }

    public Friend(String id, String first, String last, String fullName, String picture,
                  String cover, String email) {
        this.id = id;
        this.firstName = first;
        this.lastName = last;
        this.fullName = fullName;
        this.picture = picture;
        this.cover = cover;
        this.email = email;
    }
}
