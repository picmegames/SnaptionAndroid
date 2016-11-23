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
   public static final String sImage = "image";
   public static final String sCaptions = "captions";

   @SerializedName(sId) public String id;
   @SerializedName(sStartDate) public long startDate;
   @SerializedName(sEndDate) public long endDate;
   @SerializedName(sIsPrivate) public boolean isPrivate;
   @SerializedName(sImage) public byte[] image;
   @SerializedName(sCaptions) public List<Caption> captions;

   @ParcelConstructor
   public Snaption(long startDate, long endDate, boolean isPrivate,
                   byte[] image, List<Caption> captions) {
      this.startDate = startDate;
      this.endDate = endDate;
      this.isPrivate = isPrivate;
      this.image = image;
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

      return id.equals(snaption.id);
   }

   @Override
   public int hashCode() {
      return id.hashCode();
   }
}
