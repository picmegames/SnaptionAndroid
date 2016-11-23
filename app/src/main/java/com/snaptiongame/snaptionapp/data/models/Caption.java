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
   public static final String sCaption = "caption";

   @SerializedName(sId) public String id;
   @SerializedName(sSnaptionId) public String snaptionId;
   @SerializedName(sCaption) public String caption;

   @ParcelConstructor
   public Caption(String snaptionId, String caption) {
      this.snaptionId = snaptionId;
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

      return id.equals(caption.id);
   }

   @Override
   public int hashCode() {
      return id.hashCode();
   }
}
