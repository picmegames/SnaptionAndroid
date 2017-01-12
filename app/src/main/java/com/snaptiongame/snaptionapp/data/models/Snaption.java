package com.snaptiongame.snaptionapp.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author Tyler Wong
 */
public final class Snaption {
   public static final String sId = "pk";
   public static final String sFields = "fields";

   @SerializedName(sId) public int id;
   @SerializedName(sFields) public SnaptionMeta meta;

   public Snaption(int id, SnaptionMeta meta) {
      this.id = id;
      this.meta = meta;
   }
}
