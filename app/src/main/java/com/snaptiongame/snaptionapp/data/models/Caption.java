package com.snaptiongame.snaptionapp.data.models;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * @author Tyler Wong
 */
@Parcel()
public final class Caption implements Parcelable {
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

   protected Caption(android.os.Parcel in) {

      id = in.readInt();
      snaptionId = in.readInt();
      userId = in.readInt();
      username = in.readString();
      upvotes = in.readInt();
      caption = in.readString();
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

   @Override
   public int describeContents() {
      return 0;
   }

   @Override
   public void writeToParcel(android.os.Parcel dest, int i) {
      dest.writeInt(id);
      dest.writeInt(snaptionId);
      dest.writeInt(userId);
      dest.writeString(username);
      dest.writeInt(upvotes);
      dest.writeString(caption);
   }

   public static final Parcelable.Creator<Caption> CREATOR = new Parcelable.Creator<Caption>() {

      @Override
      public Caption createFromParcel(android.os.Parcel in) {
         return new Caption(in);
      }

      @Override
      public Caption[] newArray(int size) {
         return new Caption[size];
      }
   };
}
