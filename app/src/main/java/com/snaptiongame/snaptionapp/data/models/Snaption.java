package com.snaptiongame.snaptionapp.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * @author Tyler Wong
 */
@Parcel()
public final class Snaption {
   public static final String sId = "pk";
   public static final String sFields = "fields";

   @SerializedName(sId) public int id;
   @SerializedName(sFields) public SnaptionMeta meta;

   @ParcelConstructor
   public Snaption(int id, SnaptionMeta meta) {
      this.id = id;
      this.meta = meta;
   }
}
