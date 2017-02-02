package com.snaptiongame.snaptionapp.data.models;

/**
 * @author Tyler Wong
 */

public class Like {
   public int userId;
   public int targetId;
   public boolean upvote;
   public boolean flagged;
   public String type;

   public static final String USER_ID = "userId";
   public static final String GAME_ID = "gameId";
   public static final String CAPTION_ID = "captionId";
   public static final String UPVOTE = "upvote";
   public static final String FLAGGED = "flagged";
   public static final String BEEN_UPVOTED = "beenUpvoted";
   public static final String BEEN_FLAGGED = "beenFlagged";

   public Like(int userId, int targetId, boolean upvote, boolean flagged, String type) {
      this.userId = userId;
      this.targetId = targetId;
      this.upvote = upvote;
      this.flagged = flagged;
      this.type = type;
   }
}
