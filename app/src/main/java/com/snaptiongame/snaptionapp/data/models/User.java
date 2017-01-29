package com.snaptiongame.snaptionapp.data.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Tyler Wong
 */

public class User extends RealmObject {

   @SerializedName(ID)
   @PrimaryKey
   public int id;
   @SerializedName(USERNAME)
   public String username;
   @SerializedName(PICTURE)
   public String picture;

   public static final String ID = "id";
   public static final String USERNAME = "username";
   public static final String PICTURE = "picture";
   public static final int MAX_NAME_LENGTH = 15;

   public User() {

   }

   public User(String change) {
      if (change.length() > MAX_NAME_LENGTH) {
         this.picture = change;
      }
      else {
         this.username = change;
      }
   }

   public User(String username, String picture) {
      this.username = username;
      this.picture = picture;
   }
}
