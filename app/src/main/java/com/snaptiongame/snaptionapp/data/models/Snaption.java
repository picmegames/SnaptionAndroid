package com.snaptiongame.snaptionapp.data.models;

import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tyler Wong
 */
@Parcel()
public final class Snaption implements Parcelable{
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

   public Snaption(android.os.Parcel in) {




      this.id = in.readInt();
      //Log.i("ID", String.valueOf(id));
      this.startDate = in.readLong();
      //Log.i("startDate", String.valueOf(startDate));
      this.endDate = in.readLong();
      //Log.i("endDate", String.valueOf(endDate));
      this.isPrivate = in.readByte() != 0x00;
      //Log.i("isPrivate", String.valueOf(isPrivate));
      this.pickerName = in.readString();
      //Log.i("pickerName", String.valueOf(pickerName));
      this.pickerId = in.readInt();
      //Log.i("pickerID", String.valueOf(pickerId));
      this.image = new byte[in.readInt()];

      in.readByteArray(this.image);
      this.imageUrl = in.readString();
      if (in.readByte() == 0x01) {
         this.captions = new ArrayList<Caption>();
         in.readList(captions, Caption.class.getClassLoader());
      }
      else{
         captions = null;
      }
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

   @Override
   public int describeContents() {
      return 0;
   }

   @Override
   public void writeToParcel(android.os.Parcel dest, int i) {
      dest.writeInt(id);
      dest.writeLong(startDate);
      dest.writeLong(endDate);

      dest.writeByte((byte) (isPrivate ? 0x01 : 0x00));
      dest.writeString(pickerName);
      dest.writeInt(pickerId);
      dest.writeInt(image.length);

      dest.writeByteArray(image);
      dest.writeString(imageUrl);
      if (captions == null) {
         dest.writeByte((byte) (0x00));
      } else {
         dest.writeByte((byte) (0x01));
         dest.writeList(captions);
      }
   }

   public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {


      @Override
      public Object createFromParcel(android.os.Parcel parcel) {
         return new Snaption(parcel);
      }

      @Override
      public Snaption[] newArray(int size) {
         return new Snaption[size];
      }
   };

   @Override
   public String toString() {
      return this.pickerName;
   }
}
