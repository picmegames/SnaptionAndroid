package com.snaptiongame.app.data.api;

import com.snaptiongame.app.data.models.AddFriendRequest;
import com.snaptiongame.app.data.models.Caption;
import com.snaptiongame.app.data.models.CaptionSet;
import com.snaptiongame.app.data.models.DeepLinkRequest;
import com.snaptiongame.app.data.models.FitBCaption;
import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.data.models.Like;
import com.snaptiongame.app.data.models.OAuthRequest;
import com.snaptiongame.app.data.models.Session;
import com.snaptiongame.app.data.models.Snaption;
import com.snaptiongame.app.data.models.User;

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
public interface SnaptionApi {

    /**
     * This method authenticates a user with Facebook with
     * a POST request.
     *
     * @param request The request for authentication
     * @return An observable that emits a Session
     */
    @POST("/OAuth/Facebook/")
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
    @GET("/Users/{userId}/")
    Observable<User> getUser(@Path("userId") int userId);

    /**
     * This method sends a request to add a friend
     * with a POST request.
     *
     * @param userID        The id of the user to be added
     * @param friendRequest The AddFriendRequest body
     * @return An observable that emits an AddFriendRequest object
     */
    @POST("/UserFriends/{userId}/")
    Observable<AddFriendRequest> addFriend(@Path("userId") int userID, @Body AddFriendRequest friendRequest);

    /**
     * This method sends a request to delete a friend
     * with a custom DELETE request.
     *
     * @param userID        The id of the user to be deleted
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
    @GET("/Users?email=")
    Observable<User> getUserByEmail(@Query("email") String userEmail);

    /**
     * This method sends a request for a user with a GET request.
     *
     * @param facebookID The desired user's facebookID
     * @return An observable that emits a User object
     */
    @GET("/Users?facebookID=")
    Observable<User> getUserByFacebook(@Query("facebookID") String facebookID);

    /**
     * This method sends a request to update a user with a PUT request.
     *
     * @param user The new updated information for the user
     * @return An observable that emits a User object
     */
    @PUT("/Users/")
    Observable<User> updateUser(@Body User user);

    /**
     * This method sends a request to get a list of games
     * with a GET request.
     *
     * @return An observable that emits a list of Snaption objects.
     */
    @GET("/Games")
    Observable<List<Snaption>> getSnaptions(@Query("private") boolean isPrivate);

    /**
     * This method sends a request to get a single game
     * with a GET request.
     *
     * @param gameId the id of the game
     * @return An observable that emits a list of Snaption objects.
     */
    @GET("/Games/{gameId}/")
    Observable<Snaption> getSnaption(@Path("gameId") int gameId);

    /**
     * This method sends a request to upvote or flag a game with
     * a PUT request.
     *
     * @param request The Like body
     * @return An observable that emits a Like object
     */
    @PUT("/UserXGame/")
    Observable<Like> upvoteOrFlagSnaption(@Body Like request);

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
     * @param gameId  The id of the game we are adding a caption to
     * @param caption The caption to be added
     * @return An observable that emits a Caption object
     */
    @POST("/Games/{game_id}/Captions/")
    Observable<Caption> addCaption(@Path("game_id") int gameId, @Body Caption caption);

    /**
     * This method sends a request to upvote or flag a caption
     * with a PUT request.
     *
     * @param request The Like body
     * @return An observable that emits a Like object
     */
    @PUT("/UserXCaption/")
    Observable<Like> upvoteOrFlagCaption(@Body Like request);

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
     * have logged into Snaption
     *
     * @return An observable that emits a list of Friends
     */
    @GET("/Social/Friends/")
    Observable<List<Friend>> getFacebookFriends();

    /**
     * This method sends a request for a deep link token that will be used to
     * generate a branch.io link
     *
     * @return An observable that emits a deep link token
     */
    @POST("/DeepLink/")
    Observable<String> getToken(@Body DeepLinkRequest deepLinkRequest);
}
