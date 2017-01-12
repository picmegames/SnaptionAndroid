package com.snaptiongame.snaptionapp.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author Tyler Wong
 */
public class SnaptionMeta {
   public static final String sStartDate = "start_date";
   public static final String sEndDate = "end_date";
   public static final String sIsPrivate = "is_private";
   public static final String sPickerId = "picker_id";
   public static final String sPickerName = "picker_name";
   public static final String sType = "type";
   public static final String sImage = "pictureEncoded";
   public static final String sImageUrl = "picture";
   public static final String sTopCaptioner = "topCaptioner";
   public static final String sTopCaption = "topCaption";
   public static final String sTopCaptionerImage = "topCaptionerImage";

   @SerializedName(sStartDate) public long startDate;
   @SerializedName(sEndDate) public long endDate;
   @SerializedName(sIsPrivate) public boolean isPrivate;
   @SerializedName(sType) public String type;
   @SerializedName(sPickerName) public String pickerName;
   @SerializedName(sPickerId) public int pickerId;
   @SerializedName(sImage) public String encodedImage;
   @SerializedName(sImageUrl) public String picture;
   @SerializedName(sTopCaptionerImage) public String topCaptionerImage;
   @SerializedName(sTopCaptioner) public String topCaptioner;
   @SerializedName(sTopCaption) public String topCaption;

   public SnaptionMeta(long startDate, long endDate, boolean isPrivate, String pickerName,
                       int pickerId, String encodedImage, String picture, String type,
                       String topCaptionerImage, String topCaptioner, String topCaption) {
      this.startDate = startDate;
      this.endDate = endDate;
      this.isPrivate = isPrivate;
      this.pickerName = pickerName;
      this.pickerId = pickerId;
      this.encodedImage = encodedImage;
      this.picture = picture;
      this.type = type;
      this.topCaptionerImage = topCaptionerImage;
      this.topCaptioner = topCaptioner;
      this.topCaption = topCaption;
   }
}
