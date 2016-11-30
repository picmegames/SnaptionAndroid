package com.snaptiongame.snaptionapp.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.List;

/**
 * @author Tyler Wong
 */
@Parcel()
public final class Snaption {
   public static final String sId = "id";
   public static final String sStartDate = "start_date";
   public static final String sEndDate = "end_date";
   public static final String sIsPrivate = "is_private";
   public static final String sPickerId = "picker_id";
   public static final String sPickerName = "picker_name";
   public static final String sImage = "image";
   public static final String sImageUrl = "image_url";
   public static final String sCaptions = "captions";

   @SerializedName(sId) public int id;
   @SerializedName(sStartDate) public long startDate;
   @SerializedName(sEndDate) public long endDate;
   @SerializedName(sIsPrivate) public boolean isPrivate;
   @SerializedName(sPickerName) public String pickerName;
   @SerializedName(sPickerId) public int pickerId;
   @SerializedName(sImage) public byte[] image;
   @SerializedName(sImageUrl) public String imageUrl;
   @SerializedName(sCaptions) public List<Caption> captions;

   @ParcelConstructor
   public Snaption(int id, long startDate, long endDate, boolean isPrivate, String pickerName,
                   int pickerId, byte[] image, String imageUrl, List<Caption> captions) {
      this.id = id;
      this.startDate = startDate;
      this.endDate = endDate;
      this.isPrivate = isPrivate;
      this.pickerName = pickerName;
      this.pickerId = pickerId;
      this.image = image;
      this.imageUrl = imageUrl;
      this.captions = captions;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }
      Snaption snaption = (Snaption) o;

      return id == snaption.id;
   }

   @Override
   public int hashCode() {
      return id;
   }
}
