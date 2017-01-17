package com.snaptiongame.snaptionapp.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author Tyler Wong
 */
public final class Caption {
   public static final String sId = "id";
   public static final String sCaption = "userEntry";
   public static final String sPicture = "picUrl";

   @SerializedName(sId)
   public int id;
   @SerializedName(sCaption)
   public String caption;
   @SerializedName(sPicture)
   public String picture;

   public Caption(int id, String caption, String picUrl) {
      this.id = id;
      this.caption = caption;
      this.picture = picture;
   }
}
