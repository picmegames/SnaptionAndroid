package com.snaptiongame.snaptionapp.data.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Tyler Wong
 */
public class Caption extends RealmObject {

   @SerializedName(ID)
   @PrimaryKey
   public int id;
   @SerializedName(GAME_ID)
   public int gameId;
   @SerializedName(NUM_VOTES)
   public int numVotes;
   @SerializedName(USER_ID)
   public int creatorId;
   @SerializedName(FITB_ID_RECEIVE)
   public int fitBId;
   @SerializedName(CAPTION)
   public String caption;
   @SerializedName(PICTURE)
   public String picture;
   @SerializedName(FITB_ID_SEND)
   public int fitbIdSend;

   public static final String ID = "id";
   public static final String GAME_ID = "game_id";
   public static final String NUM_VOTES = "numVotes";
   public static final String USER_ID = "userId";
   public static final String FITB_ID_RECEIVE = "fitb_id";
   public static final String FITB_ID_SEND = "fitbId";
   public static final String CAPTION = "userEntry";
   public static final String PICTURE = "picUrl";

   public Caption() {

   }

   public Caption(int fitBId, String caption, int userId) {
      this.fitbIdSend = fitBId;
      this.caption = caption;
      this.id = userId;
   }

   public Caption(int id, int gameId, int numVotes, int creatorId, int fitBId,
                  String caption, String picture) {
      this.id = id;
      this.gameId = gameId;
      this.numVotes = numVotes;
      this.creatorId = creatorId;
      this.fitBId = fitBId;
      this.caption = caption;
      this.picture = picture;
   }
}
