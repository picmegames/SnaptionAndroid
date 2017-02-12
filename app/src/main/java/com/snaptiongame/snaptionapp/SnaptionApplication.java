package com.snaptiongame.snaptionapp;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.snaptiongame.snaptionapp.data.converters.AddFriendConverter;
import com.snaptiongame.snaptionapp.data.converters.CaptionConverter;
import com.snaptiongame.snaptionapp.data.converters.FriendConverter;
import com.snaptiongame.snaptionapp.data.converters.LikeConverter;
import com.snaptiongame.snaptionapp.data.converters.OAuthConverter;
import com.snaptiongame.snaptionapp.data.converters.SessionConverter;
import com.snaptiongame.snaptionapp.data.converters.SnaptionConverter;
import com.snaptiongame.snaptionapp.data.converters.UserConverter;
import com.snaptiongame.snaptionapp.data.models.AddFriendRequest;
import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.models.Friend;
import com.snaptiongame.snaptionapp.data.models.Like;
import com.snaptiongame.snaptionapp.data.models.OAuthRequest;
import com.snaptiongame.snaptionapp.data.models.Session;
import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.data.models.User;
import com.squareup.leakcanary.LeakCanary;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import io.realm.Realm;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

/**
 * This is the entry point for the application.
 * When the application is started up, LeakCanary, Realm, Gson,
 * and Timber are initialized.
 *
 * @author Tyler Wong
 * @version 1.0
 */
public class SnaptionApplication extends Application {
   public static Gson gson = setupGson();
   public static OkHttpClient okHttpClient;
   private static X509TrustManager trustManager;
   private static Context context;

   private static final String CERT_TYPE = "X.509";
   private static final String CA = "ca";
   private static final String TLS = "TLS";

   @Override
   public void onCreate() {
      super.onCreate();
      SnaptionApplication.context = getApplicationContext();

      // INIT Leak Canary (Memory leak checking)
      if (LeakCanary.isInAnalyzerProcess(this)) {
         return;
      }

      LeakCanary.install(this);

      // INIT Realm (Local database)
      Realm.init(this);

      if (BuildConfig.DEBUG) {
         // INIT Timber (Logger for debug builds)
         Timber.plant(new Timber.DebugTree());
      }
   }

   public static Context getContext() {
      return SnaptionApplication.context;
   }

   /**
    * This method provides and handles the creation of an OkHttpClient.
    * If we are an a debug build, add a logging interceptor,
    * otherwise provide no logging interceptor.
    *
    * @return The development or production OkHttpClient
    */
   public static OkHttpClient makeOkHttpClient() {
      try {
         SSLSocketFactory socketFactory = getSSLConfig(context).getSocketFactory();

         if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient = new OkHttpClient.Builder()
                  .sslSocketFactory(socketFactory, trustManager)
                  // TODO Make this more secure
                  .hostnameVerifier((String s, SSLSession sslSession) -> true)
                  .addInterceptor(interceptor)
                  .build();
         }
         else {
            okHttpClient = new OkHttpClient.Builder()
                  .sslSocketFactory(socketFactory, trustManager)
                  // TODO Make this more secure
                  .hostnameVerifier((String s, SSLSession sslSession) -> true)
                  .build();
         }
      }
      catch (CertificateException | KeyStoreException | NoSuchAlgorithmException |
            KeyManagementException | IOException e) {
         Timber.e("Could not initialize OkHttpClient with SSL Certificate", e);

         okHttpClient = new OkHttpClient.Builder()
               .build();
      }

      return okHttpClient;
   }

   /**
    * This method will open the .crt file in the raw resource folder
    * and create the necessary objects to correctly provide a secure
    * connection between our application and the server.
    *
    * @return A new instance of an SSLContext
    * @throws CertificateException
    * @throws KeyStoreException
    * @throws NoSuchAlgorithmException
    * @throws KeyManagementException
    * @throws IOException
    */
   private static SSLContext getSSLConfig(Context context) throws CertificateException,
         KeyStoreException, NoSuchAlgorithmException, KeyManagementException, IOException {

      CertificateFactory certificateFactory;
      certificateFactory = CertificateFactory.getInstance(CERT_TYPE);

      // Open certificate from raw resource
      Certificate certificate;
      try (InputStream cert = context.getResources().openRawResource(R.raw.wwwexamplecom)) {
         certificate = certificateFactory.generateCertificate(cert);
      }

      // Creating a KeyStore containing our trusted CAs
      String keyStoreType = KeyStore.getDefaultType();
      KeyStore keyStore = KeyStore.getInstance(keyStoreType);
      keyStore.load(null, null);
      keyStore.setCertificateEntry(CA, certificate);

      // Creating a TrustManager that trusts the CAs in our KeyStore.
      String trustManagerAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
      TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(trustManagerAlgorithm);
      trustManagerFactory.init(keyStore);

      // Find correct X509TrustManager
      TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
      for (TrustManager manager : trustManagers) {
         if (manager != null) {
            trustManager = (X509TrustManager) manager;
         }
      }

      // Creating an SSLSocketFactory that uses our TrustManager
      SSLContext sslContext = SSLContext.getInstance(TLS);
      sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

      return sslContext;
   }

   /**
    * This method provides and handles the creation of a Gson parser.
    * It will add all of the necessary Type Adapters to serialize and
    * deserialize objects passed to and from the server.
    *
    * @return The Gson parser to be used with a Retrofit instance
    */
   public static Gson setupGson() {
      GsonBuilder builder = new GsonBuilder();
      builder.registerTypeAdapter(OAuthRequest.class, new OAuthConverter());
      builder.registerTypeAdapter(Session.class, new SessionConverter());
      builder.registerTypeAdapter(User.class, new UserConverter());
      builder.registerTypeAdapter(Snaption.class, new SnaptionConverter());
      builder.registerTypeAdapter(Caption.class, new CaptionConverter());
      builder.registerTypeAdapter(Like.class, new LikeConverter());
      builder.registerTypeAdapter(Friend.class, new FriendConverter());
      builder.registerTypeAdapter(AddFriendRequest.class, new AddFriendConverter());
      builder.excludeFieldsWithoutExposeAnnotation();
      return builder.create();
   }
}
