package com.snaptiongame.snaptionapp.data.models;

/**
 * @author Tyler Wong
 */

public class Like {
   public int userId;
   public boolean upvote;

   public static final String USER_ID = "userId";
   public static final String UPVOTE = "upvote";

   public Like(int userId, boolean upvote) {
      this.userId = userId;
      this.upvote = upvote;
   }
}
