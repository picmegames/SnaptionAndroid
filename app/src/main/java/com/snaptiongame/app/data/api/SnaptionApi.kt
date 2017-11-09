package com.snaptiongame.app.data.api

import com.snaptiongame.app.data.models.*

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * This interface outlines the REST API that the application
 * will communicate with.
 *
 * @author Tyler Wong
 * @version 1.0
 */
interface SnaptionApi {

    /**
     * This method authenticates a user with Facebook with
     * a POST request.
     *
     * @param request The request for authentication
     * @return A single that emits a Session
     */
    @POST("/OAuth/Facebook/")
    fun userOAuthFacebook(@Body request: OAuthRequest): Single<Session>

    /**
     * This method authenticates a user with Google on with
     * a POST request.
     *
     * @param request The request for authentication
     * @return A single that emits a Session object
     */
    @POST("/OAuth/Google/")
    fun userOAuthGoogle(@Body request: OAuthRequest): Single<Session>

    /**
     * This method will log a user out with a GET request.
     *
     * @return A completable
     */
    @GET("/Logout/")
    fun logout(): Completable

    /**
     * This method will send a GET request to the server to determine
     * if the currently logged in user's session is still valid.
     *
     * @return A single boolean true if the session is valid, else false
     */
    @GET("/OAuth/Status/")
    fun isSessionValid(): Single<Boolean>

    /**
     * This method will send a GET request to the server to update a
     * user's device token
     *
     * @param deviceToken The currently logged in user's device token
     * @return A completable
     */
    @GET("/Notifications/Refresh/")
    fun refreshNotificationToken(@Query("device_token") deviceToken: String): Completable

    /**
     * This method sends a request for a user with a GET request.
     *
     * @param userId The id of the desired user.
     * @return A single that emits a User object
     */
    @GET("/Users/{userId}/")
    fun getUser(@Path("userId") userId: Int): Single<User>

    /**
     * This method sends a request to add a friend
     * with a POST request.
     *
     * @param friendRequest The AddFriendRequest body
     * @return An observable that emits an AddFriendRequest object
     */
    @POST("/UserFriends/")
    fun addFriend(@Body friendRequest: AddFriendRequest): Single<AddFriendRequest>

    /**
     * This method sends a request to delete a friend
     * with a custom DELETE request.
     *
     * @param friendRequest The AddFriendRequest body
     * @return A completable that emits an AddFriendRequest object
     */
    @HTTP(method = "DELETE", path = "/UserFriends/", hasBody = true)
    fun deleteFriend(@Body friendRequest: AddFriendRequest): Completable

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
    fun searchUsers(@Query("email") email: String?,
                    @Query("facebookId") facebookId: String?,
                    @Query("username") username: String?,
                    @Query("fullName") fullName: String?,
                    @Query("page") page: Int): Flowable<List<User>>

    /**
     * This method sends a request to update a user with a PUT request.
     *
     * @param user The new updated information for the user
     * @return A single that emits a User object
     */
    @PUT("/Users/")
    fun updateUser(@Body user: User): Single<User>

    /**
     * This method sends a request to get a list of the current
     * user's games with a GET request.
     *
     * @return An observable that emits a list of Game objects.
     */
    @GET("/Games/mine/")
    fun getGamesMine(@Query("tag") tags: List<String>?,
                     @Query("status") status: String?,
                     @Query("page") page: Int): Flowable<List<Game>>

    /**
     * This method sends a request to get a list of the current
     * discover games with a GET request.
     *
     * @return An observable that emits a list of Game objects.
     */
    @GET("/Games/discover/")
    fun getGamesDiscover(@Query("tag") tags: List<String>?,
                         @Query("status") status: String?,
                         @Query("page") page: Int): Flowable<List<Game>>

    /**
     * This method sends a request to get a list of the current
     * popular games with a GET request.
     *
     * @return An observable that emits a list of Game objects.
     */
    @GET("/Games/popular/")
    fun getGamesPopular(@Query("tag") tags: List<String>?,
                        @Query("status") status: String?,
                        @Query("page") page: Int): Flowable<List<Game>>

    /**
     * This method sends a request to get a list of the games
     * a user has been involved in with a GET request.
     *
     * @return An observable that emits a list of Game objects.
     */
    @GET("/UserGame/History/")
    fun getGamesHistory(@Query("creator") userId: Int, @Query("page") page: Int): Flowable<List<Game>>

    /**
     * This method sends a request to get a single game
     * with a GET request.
     *
     * @param gameId the id of the game
     * @return A single that emits a list of Game objects.
     */
    @GET("/Games/{gameId}/")
    fun getGame(@Path("gameId") gameId: Int, @Query("linkToken") token: String?): Single<Game>

    /**
     * This method sends a request to upvote or flag a game with
     * a PUT request.
     *
     * @param request The GameAction body
     * @return A completable that emits a GameAction object
     */
    @PUT("/UserXGame/")
    fun upvoteOrFlagGame(@Body request: GameAction): Completable

    /**
     * This method sends a request to add a game with
     * a POST request.
     *
     * @param snaption The game to be added
     * @return A completable that emits a Game object
     */
    @POST("/Games/")
    fun addGame(@Body snaption: Game): Completable

    /**
     * This method sends a request to get a user's friends
     * with a GET request.
     *
     * @return An observable that emits a list of Friend objects
     */
    @GET("/UserFriends/")
    fun getFriends(@Query("page") page: Int): Flowable<List<Friend>>

    /**
     * This method sends a request to get a user's friends
     * with a GET request.
     *
     * @return An observable that emits a list of Friend objects
     */
    @GET("/UserFriends/Followers/")
    fun getFollowers(): Flowable<List<Friend>>

    /**
     * This method sends a request to get a list of captions
     * with a GET request.
     *
     * @param gameId The id of the game whose captions we want
     * @return An observable that emits a list of Caption objects
     */
    @GET("/Games/{gameId}/Captions/")
    fun getCaptions(@Path("gameId") gameId: Int, @Query("page") page: Int): Flowable<List<Caption>>

    /**
     * This method sends a request to add a caption to a game
     * with a POST request.
     *
     * @param gameId  The id of the game we are adding a caption to
     * @param caption The caption to be added
     * @return A completable that emits a Caption object
     */
    @POST("/Games/{game_id}/Captions/")
    fun addCaption(@Path("game_id") gameId: Int, @Body caption: Caption): Completable

    /**
     * This method sends a request to upvote or flag a caption
     * with a PUT request.
     *
     * @param request The GameAction body
     * @return A completable that emits a GameAction object
     */
    @PUT("/UserXCaption/")
    fun upvoteOrFlagCaption(@Body request: GameAction): Completable

    /**
     * This method sends a request to retrieve all fill in the blank from snaption.
     * Uses a GET request
     *
     * @return An observable that emits a list of Fill in the Blank Captions
     */
    @GET("/FitBSet/{set_id}/")
    fun getFitBCaptions(@Path("set_id") setId: Int): Flowable<List<FitBCaption>>

    /**
     * This method sends a request to retrieve all Caption Sets available to a user
     *
     * @return An observable that emits a list of Caption Sets
     */
    @GET("/FitBSet/")
    fun getCaptionSets(): Flowable<List<CaptionSet>>

    /**
     * This method sends a request to retrieve all the user's Facebook friends that
     * have logged into Game
     *
     * @return An observable that emits a list of Friends
     */
    @GET("/Social/Friends/")
    fun getFacebookFriends(): Flowable<List<Friend>>

    /**
     * This method sends a request for a deep link token that will be used to
     * generate a branch.io link
     *
     * @return A single that emits a deep link token
     */
    @POST("/DeepLink/")
    fun getToken(@Body deepLinkRequest: DeepLinkRequest): Single<String>

    /**
     * This method sends a request to retrieve the user's stats
     *
     * @return A single that emits a UserStats object
     */
    @GET("/Users/Stats/{userId}/")
    fun getUserStats(@Path("userId") userId: Int): Single<UserStats>

    /**
     * This method sends a request to retrieve the user's activity feed
     *
     * @return A single that emits a list of ActivityFeedItem objects
     */
    @GET("/Activity/")
    fun getActivityFeed(@Query("page") page: Int): Single<List<ActivityFeedItem>>

    /**
     * This method sends a request to retrieve the user's friend leaderboard
     *
     * @return A single that emits a list of users
     */
    @GET("/Users/Leaderboard/")
    fun getUserLeaderboard(): Flowable<List<User>>

    /**
     * This method sends a request to retrieve the user's activity feed
     *
     * @return A single that emits a list of Offer objects
     */
    @GET("/Shop/")
    fun getAllOffers(): Flowable<List<Offer>>
}
