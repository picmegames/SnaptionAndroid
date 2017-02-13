package com.snaptiongame.snaptionapp.data.providers;

import com.snaptiongame.snaptionapp.data.models.User;
import com.snaptiongame.snaptionapp.data.providers.api.ApiProvider;
import com.snaptiongame.snaptionapp.data.services.SnaptionApiService;

import io.reactivex.Observable;

/**
 * @author Tyler Wong
 */

public class UserProvider {
   private static SnaptionApiService apiService = ApiProvider.getApiService();

   public static Observable<User> getUser(int userId) {
      return apiService.getUser(userId);
   }

   public static Observable<User> getUserWithEmail(String email) {
      return apiService.getUserByEmail(email);
   }

   public static Observable<User> getUserWithFacebook(String id) {
      return apiService.getUserByFacebook(id);
   }

   public static Observable<User> updateUser(int userId, User user) {
      return apiService.updateUser(userId, user);
   }
}
