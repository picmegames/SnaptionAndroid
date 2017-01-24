package com.snaptiongame.snaptionapp.data.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Tyler Wong
 */
public class Snaption extends RealmObject {
   public static final String sId = "id";
   public static final String sStartDate = "start_date";
   public static final String sEndDate = "end_date";
   public static final String sIsPublic = "isPublic";
   public static final String sPickerId = "picker_id";
   public static final String sPickerName = "picker_name";
   public static final String sType = "type";
   public static final String sImage = "pictureEncoded";
   public static final String sImageUrl = "picture";
   public static final String sTopCaption = "topCaption";

   @SerializedName(sId)
   @PrimaryKey
   public int id;
   @SerializedName(sStartDate)
   public long startDate;
   @SerializedName(sEndDate)
   public long endDate;
   @SerializedName(sIsPublic)
   public boolean isPublic;
   @SerializedName(sType)
   public String type;
   @SerializedName(sPickerName)
   public String pickerName;
   @SerializedName(sPickerId)
   public int pickerId;
   @SerializedName(sImage)
   public String encodedImage;
   @SerializedName(sImageUrl)
   public String picture;
   @SerializedName(sTopCaption)
   public Caption topCaption;

   public Snaption() {

   }

   public Snaption(int id, long startDate, long endDate, boolean isPublic, String pickerName,
                   int pickerId, String encodedImage, String picture, String type) {
      this.id = id;
      this.startDate = startDate;
      this.endDate = endDate;
      this.isPublic = isPublic;
      this.pickerName = pickerName;
      this.pickerId = pickerId;
      this.encodedImage = encodedImage;
      this.picture = picture;
      this.type = type;
   }
}
