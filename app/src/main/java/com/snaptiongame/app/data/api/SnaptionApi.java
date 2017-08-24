package com.snaptiongame.app.data.api;

import com.snaptiongame.app.data.models.ActivityFeedItem;
import com.snaptiongame.app.data.models.AddFriendRequest;
import com.snaptiongame.app.data.models.Caption;
import com.snaptiongame.app.data.models.CaptionSet;
import com.snaptiongame.app.data.models.DeepLinkRequest;
import com.snaptiongame.app.data.models.FitBCaption;
import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.models.GameAction;
import com.snaptiongame.app.data.models.OAuthRequest;
import com.snaptiongame.app.data.models.Session;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.data.models.UserStats;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
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
public interface SnaptionApi {

    /**
     * This method authenticates a user with Facebook with
     * a POST request.
     *
     * @param request The request for authentication
     * @return A single that emits a Session
     */
    @POST("/OAuth/Facebook/")
    Single<Session> userOAuthFacebook(@Body OAuthRequest request);

    /**
     * This method authenticates a user with Google on with
     * a POST request.
     *
     * @param request The request for authentication
     * @return A single that emits a Session object
     */
    @POST("/OAuth/Google/")
    Single<Session> userOAuthGoogle(@Body OAuthRequest request);

    /**
     * This method will log a user out with a GET request.
     *
     * @return A completable
     */
    @GET("/Logout/")
    Completable logout();

    /**
     * This method will send a GET request to the server to determine
     * if the currently logged in user's session is still valid.
     *
     * @return A single boolean true if the session is valid, else false
     */
    @GET("/OAuth/Status/")
    Single<Boolean> isSessionValid();

    /**
     * This method will send a GET request to the server to update a
     * user's device token
     *
     * @param deviceToken The currently logged in user's device token
     * @return A completable
     */
    @GET("/Notifications/Refresh/")
    Completable refreshNotificationToken(@Query("device_token") String deviceToken);

    /**
     * This method sends a request for a user with a GET request.
     *
     * @param userId The id of the desired user.
     * @return A single that emits a User object
     */
    @GET("/Users/{userId}/")
    Single<User> getUser(@Path("userId") int userId);

    /**
     * This method sends a request to add a friend
     * with a POST request.
     *
     * @param friendRequest The AddFriendRequest body
     * @return An observable that emits an AddFriendRequest object
     */
    @POST("/UserFriends/")
    Single<AddFriendRequest> addFriend(@Body AddFriendRequest friendRequest);

    /**
     * This method sends a request to delete a friend
     * with a custom DELETE request.
     *
     * @param friendRequest The AddFriendRequest body
     * @return A completable that emits an AddFriendRequest object
     */
    @HTTP(method = "DELETE", path = "/UserFriends/", hasBody = true)
    Completable deleteFriend(@Body AddFriendRequest friendRequest);

    /**
     * This method sends a request for a user with a GET request.
     *
     * @param email The desired user's E-mail address
     * @param facebookId The desired user's facebook id
     * @param username The desired user's username
     * @param fullName The desired user's fullName
     * @return An observable that emits a User object
     */
    @GET("/Users/")
    Observable<List<User>> searchUsers(@Query("email") String email,
                                       @Query("facebookId") String facebookId,
                                       @Query("username") String username,
                                       @Query("fullName") String fullName,
                                       @Query("page") int page);

    /**
     * This method sends a request to update a user with a PUT request.
     *
     * @param user The new updated information for the user
     * @return A single that emits a User object
     */
    @PUT("/Users/")
    Single<User> updateUser(@Body User user);

    /**
     * This method sends a request to get a list of the current
     * user's games with a GET request.
     *
     * @return An observable that emits a list of Game objects.
     */
    @GET("/Games/mine/")
    Observable<List<Game>> getGamesMine(@Query("tag") List<String> tags,
                                        @Query("status") String status,
                                        @Query("page") int page);

    /**
     * This method sends a request to get a list of the current
     * discover games with a GET request.
     *
     * @return An observable that emits a list of Game objects.
     */
    @GET("/Games/discover/")
    Observable<List<Game>> getGamesDiscover(@Query("tag") List<String> tags,
                                            @Query("status") String status,
                                            @Query("page") int page);

    /**
     * This method sends a request to get a list of the current
     * popular games with a GET request.
     *
     * @return An observable that emits a list of Game objects.
     */
    @GET("/Games/popular/")
    Observable<List<Game>> getGamesPopular(@Query("tag") List<String> tags,
                                           @Query("status") String status,
                                           @Query("page") int page);
  
    /**
     * This method sends a request to get a list of the games
     * a user has been involved in with a GET request.
     *
     * @return An observable that emits a list of Game objects.
     */
    @GET("/UserGame/History/")
    Observable<List<Game>> getGamesHistory(@Query("creator") int userId, @Query("page") int page);

    /**
     * This method sends a request to get a single game
     * with a GET request.
     *
     * @param gameId the id of the game
     * @return A single that emits a list of Game objects.
     */
    @GET("/Games/{gameId}/")
    Single<Game> getGame(@Path("gameId") int gameId, @Query("linkToken") String token);

    /**
     * This method sends a request to upvote or flag a game with
     * a PUT request.
     *
     * @param request The GameAction body
     * @return A completable that emits a GameAction object
     */
    @PUT("/UserXGame/")
    Completable upvoteOrFlagGame(@Body GameAction request);

    /**
     * This method sends a request to add a game with
     * a POST request.
     *
     * @param snaption The game to be added
     * @return A completable that emits a Game object
     */
    @POST("/Games/")
    Completable addGame(@Body Game snaption);

    /**
     * This method sends a request to get a user's friends
     * with a GET request.
     *
     * @return An observable that emits a list of Friend objects
     */
    @GET("/UserFriends/")
    Observable<List<Friend>> getFriends(@Query("page") int page);

    /**
     * This method sends a request to get a user's friends
     * with a GET request.
     *
     * @return An observable that emits a list of Friend objects
     */
    @GET("/UserFriends/Followers/")
    Observable<List<Friend>> getFollowers();

    /**
     * This method sends a request to get a list of captions
     * with a GET request.
     *
     * @param gameId The id of the game whose captions we want
     * @return An observable that emits a list of Caption objects
     */
    @GET("/Games/{gameId}/Captions/")
    Observable<List<Caption>> getCaptions(@Path("gameId") int gameId, @Query("page") int page);

    /**
     * This method sends a request to add a caption to a game
     * with a POST request.
     *
     * @param gameId  The id of the game we are adding a caption to
     * @param caption The caption to be added
     * @return A completable that emits a Caption object
     */
    @POST("/Games/{game_id}/Captions/")
    Completable addCaption(@Path("game_id") int gameId, @Body Caption caption);

    /**
     * This method sends a request to upvote or flag a caption
     * with a PUT request.
     *
     * @param request The GameAction body
     * @return A completable that emits a GameAction object
     */
    @PUT("/UserXCaption/")
    Completable upvoteOrFlagCaption(@Body GameAction request);

    /**
     * This method sends a request to retrieve all fill in the blank from snaption.
     * Uses a GET request
     *
     * @return An observable that emits a list of Fill in the Blank Captions
     */
    @GET("/FitBSet/{set_id}/")
    Observable<List<FitBCaption>> getFitBCaptions(@Path("set_id") int setId);

    /**
     * This method sends a request to retrieve all Caption Sets available to a user
     *
     * @return An observable that emits a list of Caption Sets
     */
    @GET("/FitBSet/")
    Observable<List<CaptionSet>> getCaptionSets();

    /**
     * This method sends a request to retrieve all the user's Facebook friends that
     * have logged into Game
     *
     * @return An observable that emits a list of Friends
     */
    @GET("/Social/Friends/")
    Observable<List<Friend>> getFacebookFriends();

    /**
     * This method sends a request for a deep link token that will be used to
     * generate a branch.io link
     *
     * @return A single that emits a deep link token
     */
    @POST("/DeepLink/")
    Single<String> getToken(@Body DeepLinkRequest deepLinkRequest);

    /**
     * This method sends a request to retrieve the user's stats
     *
     * @return A single that emits a UserStats object
     */
    @GET("/Users/Stats/{userId}/")
    Single<UserStats> getUserStats(@Path("userId") int userId);

    /**
     * This method sends a request to retrieve the user's activity feed
     *
     * @return A single that emits a list of ActivityFeedItem objects
     */
    @GET("/Activity/")
    Single<List<ActivityFeedItem>> getActivityFeed(@Query("page") int page);
}
