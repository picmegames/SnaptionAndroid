package com.snaptiongame.app.data.providers.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.snaptiongame.app.BuildConfig;
import com.snaptiongame.app.R;
import com.snaptiongame.app.SnaptionApplication;
import com.snaptiongame.app.data.api.SnaptionApi;
import com.snaptiongame.app.data.converters.AddFriendConverter;
import com.snaptiongame.app.data.converters.BranchConverter;
import com.snaptiongame.app.data.converters.CaptionConverter;
import com.snaptiongame.app.data.converters.CaptionSetConverter;
import com.snaptiongame.app.data.converters.FitBCaptionConverter;
import com.snaptiongame.app.data.converters.FriendConverter;
import com.snaptiongame.app.data.converters.GameActionConverter;
import com.snaptiongame.app.data.converters.GameConverter;
import com.snaptiongame.app.data.converters.OAuthConverter;
import com.snaptiongame.app.data.converters.RankConverter;
import com.snaptiongame.app.data.converters.SessionConverter;
import com.snaptiongame.app.data.converters.UserConverter;
import com.snaptiongame.app.data.cookies.PersistentCookieStore;
import com.snaptiongame.app.data.models.AddFriendRequest;
import com.snaptiongame.app.data.models.Caption;
import com.snaptiongame.app.data.models.CaptionSet;
import com.snaptiongame.app.data.models.DeepLinkRequest;
import com.snaptiongame.app.data.models.FitBCaption;
import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.models.GameAction;
import com.snaptiongame.app.data.models.OAuthRequest;
import com.snaptiongame.app.data.models.Rank;
import com.snaptiongame.app.data.models.Session;
import com.snaptiongame.app.data.models.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import io.reactivex.schedulers.Schedulers;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

import static com.snaptiongame.app.SnaptionApplication.getContext;

/**
 * The Game API Provider provides an instance of
 * the API service built by Retrofit.
 *
 * @author Tyler Wong
 * @version 1.0
 */
public class ApiProvider {
    private static SnaptionApi apiService;
    private static PersistentCookieStore cookieStore;
    private static X509TrustManager trustManager;

    private static final String CERT_TYPE = "X.509";
    private static final String CA = "ca";
    private static final String TLS = "TLS";
    private static final long CONNECTION_TIMEOUT = 60;

    /**
     * This method provides and handles the creation of
     * the Game API service.
     *
     * @return An instance of a Game API service
     */
    public static SnaptionApi getApiService() {
        if (apiService == null) {
            apiService = new Retrofit.Builder()
                    .baseUrl(BuildConfig.SERVER_ENDPOINT)
                    .client(makeOkHttpClient())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .addConverterFactory(GsonConverterFactory.create(setupGson()))
                    .build()
                    .create(SnaptionApi.class);
        }

        return apiService;
    }

    /**
     * This method provides and handles the creation of an OkHttpClient.
     * If we are an a debug build, add a logging interceptor,
     * otherwise provide no logging interceptor.
     *
     * @return The development or production OkHttpClient
     */
    private static OkHttpClient makeOkHttpClient() {
        cookieStore = new PersistentCookieStore(getContext());
        CookieHandler cookieHandler = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieHandler);
        JavaNetCookieJar cookieJar = new JavaNetCookieJar(cookieHandler);
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);

        try {
            okHttpClientBuilder.cookieJar(cookieJar);

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                okHttpClientBuilder.addInterceptor(interceptor);
                okHttpClientBuilder.hostnameVerifier((String hostname, SSLSession session) -> true);
            }
            else {
                SSLSocketFactory socketFactory = getSSLConfig(SnaptionApplication.getContext()).getSocketFactory();
                okHttpClientBuilder.sslSocketFactory(socketFactory, trustManager);
            }
        }
        catch (CertificateException | KeyStoreException | NoSuchAlgorithmException |
                KeyManagementException | IOException e) {
            Timber.e("Could not initialize OkHttpClient with SSL Certificate", e);
            okHttpClientBuilder.cookieJar(cookieJar);
        }

        return okHttpClientBuilder.build();
    }

    public static void clearCookies() {
        cookieStore.removeAll();
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
        try (InputStream cert = context.getResources().openRawResource(R.raw.snaptionapicertificate)) {
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
            if (manager instanceof X509TrustManager) {
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
    private static Gson setupGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(OAuthRequest.class, new OAuthConverter());
        builder.registerTypeAdapter(Session.class, new SessionConverter());
        builder.registerTypeAdapter(User.class, new UserConverter());
        builder.registerTypeAdapter(Game.class, new GameConverter());
        builder.registerTypeAdapter(Caption.class, new CaptionConverter());
        builder.registerTypeAdapter(GameAction.class, new GameActionConverter());
        builder.registerTypeAdapter(Friend.class, new FriendConverter());
        builder.registerTypeAdapter(AddFriendRequest.class, new AddFriendConverter());
        builder.registerTypeAdapter(CaptionSet.class, new CaptionSetConverter());
        builder.registerTypeAdapter(FitBCaption.class, new FitBCaptionConverter());
        builder.registerTypeAdapter(DeepLinkRequest.class, new BranchConverter());
        builder.registerTypeAdapter(Rank.class, new RankConverter());
        builder.excludeFieldsWithoutExposeAnnotation();
        return builder.create();
    }
}
