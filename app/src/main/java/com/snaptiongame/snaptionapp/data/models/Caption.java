package com.snaptiongame.snaptionapp.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author Tyler Wong
 */
public final class Caption {
   public static final String sId = "id";
   public static final String sGameId = "game_id";
   public static final String sNumVotes = "numVotes";
   public static final String sCreatorId = "creator_id";
   public static final String sFitBId = "fitb_id";
   public static final String sCaption = "userEntry";
   public static final String sPicture = "picUrl";

   @SerializedName(sId)
   public int id;
   @SerializedName(sGameId)
   public int gameId;
   @SerializedName(sNumVotes)
   public int numVotes;
   @SerializedName(sCreatorId)
   public int creatorId;
   @SerializedName(sFitBId)
   public int fitBId;
   @SerializedName(sCaption)
   public String caption;
   @SerializedName(sPicture)
   public String picture;

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
