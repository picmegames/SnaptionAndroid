package com.snaptiongame.snaptionapp.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * @author Tyler Wong
 */
@Parcel()
public class SnaptionMeta {
   public static final String sStartDate = "start_date";
   public static final String sEndDate = "end_date";
   public static final String sIsPrivate = "is_private";
   public static final String sPickerId = "picker_id";
   public static final String sPickerName = "picker_name";
   public static final String sType = "type";
   public static final String sImage = "pictureEncoded";
   public static final String sImageUrl = "picture";
   public static final String sTopComment = "comment";

   @SerializedName(sStartDate) public long startDate;
   @SerializedName(sEndDate) public long endDate;
   @SerializedName(sIsPrivate) public boolean isPrivate;
   @SerializedName(sType) public String type;
   @SerializedName(sPickerName) public String pickerName;
   @SerializedName(sPickerId) public int pickerId;
   @SerializedName(sImage) public String encodedImage;
   @SerializedName(sImageUrl) public String picture;
   @SerializedName(sTopComment) public String comment;

   @ParcelConstructor
   public SnaptionMeta(long startDate, long endDate, boolean isPrivate, String pickerName,
                       int pickerId, String encodedImage, String picture, String type, String
                                  comment) {
      this.startDate = startDate;
      this.endDate = endDate;
      this.isPrivate = isPrivate;
      this.pickerName = pickerName;
      this.pickerId = pickerId;
      this.encodedImage = encodedImage;
      this.picture = picture;
      this.type = type;
      this.comment = comment;
   }
}
