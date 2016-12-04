package com.snaptiongame.snaptionapp.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * @author Tyler Wong
 */
@Parcel()
public class CaptionMeta {
   public static final String sSnaptionId = "gameId";
   public static final String sUserId = "user_id";
   public static final String sUsername = "username";
   public static final String sUpvotes = "upvotes";
   public static final String sCaption = "message";

   @SerializedName(sSnaptionId) public int snaptionId;
   @SerializedName(sUserId) public int userId;
   @SerializedName(sUsername) public String username;
   @SerializedName(sUpvotes) public int upvotes;
   @SerializedName(sCaption) public String caption;

   @ParcelConstructor
   public CaptionMeta(int snaptionId, int userId, String username, int upvotes, String caption) {
      this.snaptionId = snaptionId;
      this.userId = userId;
      this.username = username;
      this.upvotes = upvotes;
      this.caption = caption;
   }
}
