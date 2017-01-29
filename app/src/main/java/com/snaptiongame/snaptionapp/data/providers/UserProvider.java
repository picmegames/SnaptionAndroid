package com.snaptiongame.snaptionapp.data.providers;

import com.snaptiongame.snaptionapp.data.models.User;
import com.snaptiongame.snaptionapp.data.providers.api.SnaptionApiProvider;
import com.snaptiongame.snaptionapp.data.services.SnaptionApiService;

import rx.Observable;

/**
 * @author Tyler Wong
 */

public class UserProvider {
   private static SnaptionApiService apiService = SnaptionApiProvider.getApiService();

   public static Observable<User> updateUser(int userId, User user) {
      return apiService.updateUser(userId, user);
   }
}
