package com.snaptiongame.snaptionapp.data.models;

/**
 * @author Tyler Wong
 */

public class LikeRequest {
   public int userId;
   public boolean upvote;

   public LikeRequest(int userId, boolean upvote) {
      this.userId = userId;
      this.upvote = upvote;
   }
}
