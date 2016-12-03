package com.snaptiongame.snaptionapp.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * @author Tyler Wong
 */
@Parcel()
public final class Caption {
   public static final String sId = "id";
   public static final String sSnaptionId = "snaptionId";
   public static final String sUserId = "user_id";
   public static final String sUsername = "username";
   public static final String sUpvotes = "upvotes";
   public static final String sCaption = "caption";

   @SerializedName(sId) public int id;
   @SerializedName(sSnaptionId) public int snaptionId;
   @SerializedName(sUserId) public int userId;
   @SerializedName(sUsername) public String username;
   @SerializedName(sUpvotes) public int upvotes;
   @SerializedName(sCaption) public String caption;

   @ParcelConstructor
   public Caption(int id, int snaptionId, int userId, String username, int upvotes, String caption) {
      this.id = id;
      this.snaptionId = snaptionId;
      this.userId = userId;
      this.username = username;
      this.upvotes = upvotes;
      this.caption = caption;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }
      Caption caption = (Caption) o;

      return id == caption.id;
   }

   @Override
   public int hashCode() {
      return id;
   }
}
