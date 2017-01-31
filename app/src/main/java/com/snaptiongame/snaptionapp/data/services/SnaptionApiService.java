package com.snaptiongame.snaptionapp.data.services;

import com.snaptiongame.snaptionapp.data.models.AddFriendRequest;
import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.models.Friend;
import com.snaptiongame.snaptionapp.data.models.Like;
import com.snaptiongame.snaptionapp.data.models.OAuthRequest;
import com.snaptiongame.snaptionapp.data.models.Session;
import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.data.models.User;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import io.reactivex.Observable;
import retrofit2.http.Query;

/**
 * @author Tyler Wong
 */

public interface SnaptionApiService {

   @POST("/OAuth/")
   Observable<Session> userOAuthFacebook(@Body OAuthRequest request);

   @POST("/OAuth/Google/")
   Observable<Session> userOAuthGoogle(@Body OAuthRequest request);

   @GET("Users/{userId}/")
   Observable<User> getUser(@Path("userId") int userId);

   @POST("/UserFriends/{userId}/")
   Observable<AddFriendRequest> addUser(@Path("userId") int userID, @Body AddFriendRequest friendRequest);


   @GET("Users?email=")
   Observable<User> findUserEmail(@Query("email") String userEmail);

   @PUT("/Users/{userId}/")
   Observable<User> updateUser(@Path("userId") int userId, @Body User user);

   @GET("/Games/")
   Observable<List<Snaption>> getSnaptions();

   @PUT("/Games/{gameId}/")
   Observable<Like> upvoteSnaption(@Path("gameId") int gameId,
                                   @Body Like request);

   @POST("/Games/")
   Observable<Snaption> addSnaption(@Body Snaption snaption);

   @GET("/UserFriends/{userId}/")
   Observable<List<Friend>> getFriends(@Path("userId") int userId);

   @GET("/Games/{gameId}/Captions/")
   Observable<List<Caption>> getCaptions(@Path("gameId") int gameId);

   @POST("/Games/{game_id}/Captions/")
   Observable<Caption> addCaption(@Path("game_id") int gameId, @Body Caption caption);

   @PUT("/Captions/{captionId}/")
   Observable<Like> upvoteCaption(@Path("captionId") int captionId,
                                  @Body Like request);
}
