package com.snaptiongame.snaptionapp.data.services;

import com.snaptiongame.snaptionapp.data.models.AddFriendRequest;
import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.models.FitBCaption;
import com.snaptiongame.snaptionapp.data.models.Friend;
import com.snaptiongame.snaptionapp.data.models.Like;
import com.snaptiongame.snaptionapp.data.models.OAuthRequest;
import com.snaptiongame.snaptionapp.data.models.Session;
import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.data.models.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * This interface outlines the REST API that the application
 * will communicate with.
 *
 * @author Tyler Wong
 * @version 1.0
 */
public interface SnaptionApiService {

   /**
    * This method authenticates a user with Facebook with
    * a POST request.
    *
    * @param request The request for authentication
    * @return An observable that emits a Session
    */
   @POST("/OAuth/")
   Observable<Session> userOAuthFacebook(@Body OAuthRequest request);

   /**
    * This method authenticates a user with Google on with
    * a POST request.
    *
    * @param request The request for authentication
    * @return An observable that emits a Session object
    */
   @POST("/OAuth/Google/")
   Observable<Session> userOAuthGoogle(@Body OAuthRequest request);

   /**
    * This method sends a request for a user with a GET request.
    *
    * @param userId The id of the desired user.
    * @return An observable that emits a User object
    */
   @GET("Users/{userId}/")
   Observable<User> getUser(@Path("userId") int userId);

   /**
    * This method sends a request to add a friend
    * with a POST request.
    *
    * @param userID The id of the user to be added
    * @param friendRequest The AddFriendRequest body
    * @return An observable that emits an AddFriendRequest object
    */
   @POST("/UserFriends/{userId}/")
   Observable<AddFriendRequest> addFriend(@Path("userId") int userID, @Body AddFriendRequest friendRequest);

   /**
    * This method sends a request to delete a friend
    * with a custom DELETE request.
    *
    * @param userID The id of the user to be deleted
    * @param friendRequest The AddFriendRequest body
    * @return An observable that emits an AddFriendRequest object
    */
   @HTTP(method = "DELETE", path = "/UserFriends/{userId}/", hasBody = true)
   Observable<AddFriendRequest> deleteFriend(@Path("userId") int userID, @Body AddFriendRequest friendRequest);

   /**
    * This method sends a request for a user with a GET request.
    *
    * @param userEmail The desired user's E-mail address
    * @return An observable that emits a User object
    */
   @GET("Users?email=")
   Observable<User> getUserByEmail(@Query("email") String userEmail);

   /**
    * This method sends a request for a user with a GET request.
    *
    * @param facebookID The desired user's facebookID
    * @return An observable that emits a User object
    */
   @GET("Users?facebookID=")
   Observable<User> getUserByFacebook(@Query("facebookID") String facebookID);

   /**
    * This method sends a request to update a user with a PUT request.
    *
    * @param userId The id of the user to be updated
    * @param user The new updated information for the user
    * @return An observable that emits a User object
    */
   @PUT("/Users/{userId}/")
   Observable<User> updateUser(@Path("userId") int userId, @Body User user);

   /**
    * This method sends a request to get a list of games
    * with a GET request.
    *
    * @return An observable that emits a list of Snaption objects.
    */
   @GET("/Games/")
   Observable<List<Snaption>> getSnaptions();

   /**
    * This method sends a request to upvote a game with
    * a PUT request.
    *
    * @param request The Like body
    * @return An observable that emits a Like object
    */
   @PUT("/UserXGame/")
   Observable<Like> upvoteSnaption(@Body Like request);

   /**
    * This method sends a request to add a game with
    * a POST request.
    *
    * @param snaption The game to be added
    * @return An observable that emits a Snaption object
    */
   @POST("/Games/")
   Observable<Snaption> addSnaption(@Body Snaption snaption);

   /**
    * This method sends a request to get a user's friends
    * with a GET request.
    *
    * @param userId The id of the user whose friends we want
    * @return An observable that emits a list of Friend objects
    */
   @GET("/UserFriends/{userId}/")
   Observable<List<Friend>> getFriends(@Path("userId") int userId);

   /**
    * This method sends a request to get a list of captions
    * with a GET request.
    *
    * @param gameId The id of the game whose captions we want
    * @return An observable that emits a list of Caption objects
    */
   @GET("/Games/{gameId}/Captions/")
   Observable<List<Caption>> getCaptions(@Path("gameId") int gameId);

   /**
    * This method sends a request to add a caption to a game
    * with a POST request.
    *
    * @param gameId The id of the game we are adding a caption to
    * @param caption The caption to be added
    * @return An observable that emits a Caption object
    */
   @POST("/Games/{game_id}/Captions/")
   Observable<Caption> addCaption(@Path("game_id") int gameId, @Body Caption caption);

   /**
    * This method sends a request to upvote a caption
    * with a PUT request.
    *
    * @param request The Like body
    * @return An observable that emits a Like object
    */
   @PUT("/UserXCaption/")
   Observable<Like> upvoteCaption(@Body Like request);

   @GET("/FitB/")
   Observable<List<FitBCaption>> getFitBCaptions();
}
