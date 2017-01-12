package com.snaptiongame.snaptionapp.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author Tyler Wong
 */
public final class Caption {
   public static final String sId = "pk";
   public static final String sFields = "fields";

   @SerializedName(sId) public int id;
   @SerializedName(sFields) public CaptionMeta meta;

   public Caption(int id, CaptionMeta meta) {
      this.id = id;
      this.meta = meta;
   }
}
