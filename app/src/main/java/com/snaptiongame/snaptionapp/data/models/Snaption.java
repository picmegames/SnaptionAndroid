package com.snaptiongame.snaptionapp.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author Tyler Wong
 */
public class Snaption {

   @SerializedName(ID)
   public int id;
   @SerializedName(USER_ID)
   public int userId;
   @SerializedName(START_DATE)
   public long startDate;
   @SerializedName(END_DATE)
   public long endDate;
   @SerializedName(IS_PUBLIC)
   public boolean isPublic;
   @SerializedName(IMG_TYPE)
   public String type;
   @SerializedName(RATING)
   public int rating;
   @SerializedName(PICKER_NAME)
   public String pickerName;
   @SerializedName(PICKER_ID)
   public int pickerId;
   @SerializedName(PICTURE)
   public String picture;
   @SerializedName(TOP_CAPTION)
   public Caption topCaption;

   public static final String ID = "id";
   public static final String USER_ID = "userId";
   public static final String START_DATE = "start_date";
   public static final String END_DATE = "completedDate";
   public static final String IS_PUBLIC = "isPublic";
   public static final String PICKER_ID = "picker_id";
   public static final String PICKER_NAME = "picker_name";
   public static final String RATING = "rating";
   public static final String IMG_TYPE = "imgType";
   public static final String PICTURE = "picture";
   public static final String TOP_CAPTION = "topCaption";

   public Snaption() {

   }

   public Snaption(int userId, boolean isPublic, int rating, String picture, String type) {
      this.userId = userId;
      this.isPublic = isPublic;
      this.rating = rating;
      this.picture = picture;
      this.type = type;
   }

   public Snaption(int id, long startDate, long endDate, boolean isPublic, int rating,
                   String pickerName, int pickerId, String picture, String type) {
      this.id = id;
      this.startDate = startDate;
      this.endDate = endDate;
      this.isPublic = isPublic;
      this.rating = rating;
      this.pickerName = pickerName;
      this.pickerId = pickerId;
      this.picture = picture;
      this.type = type;
   }
}
