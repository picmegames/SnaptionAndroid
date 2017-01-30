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
   @SerializedName(TYPE)
   public String type;

   public static final String ID = "id";
   public static final String USERNAME = "username";
   public static final String PICTURE = "picture";
   public static final String TYPE = "imgType";
   public static final int MAX_NAME_LENGTH = 15;

   public User() {

   }

   public User(String username) {
      this.username = username;
   }

   public User(String picture, String type) {
      this.picture = picture;
      this.type = type;
   }
}
