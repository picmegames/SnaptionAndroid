package com.snaptiongame.snaptionapp.data.models;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.realm.annotations.PrimaryKey;

/**
 * Created by nickromero on 2/8/17.
 */

public class CaptionSet {
   //private List<Caption> mCaptions;
  // private Drawable mSetImage;

   @SerializedName(ID)
   @PrimaryKey
   public int id;
   @SerializedName(TITLE)
   public String captionSetTitle;
   @SerializedName(ACTIVE)
   public boolean isCaptionSetActive;

   public static final String TITLE = "title";
   public static final String ID = "id";
   public static final String ACTIVE = "active";

   private int mTotalCaptions;
   private int mCaptionsUnlocked;

   private static final int MAX_CAPTIONS = 10;
   private static final int MIN_CAPTIONS = 0;

   public CaptionSet(List<Caption> captions) {

      mCaptionsUnlocked = MIN_CAPTIONS;
      mTotalCaptions = MAX_CAPTIONS;

   }

   public List<Caption> getCaptions() {
      //return mCaptions;
      return new ArrayList<>();
   }

   public void setCaptions(List<Caption> captions) {
      //mCaptions = captions;
   }

   public void setCaptionsUnlocked(int unlocked) {
      mCaptionsUnlocked = unlocked;
   }

   public void setSetName(String mSetName) {
      //this.mSetName = mSetName;
   }

   public int getTotalCaptions() {
      return mTotalCaptions;
   }

   public int getCaptionsUnlocked() {
      return mCaptionsUnlocked;
   }

   public String getSetName() {

      return captionSetTitle;
   }

}
