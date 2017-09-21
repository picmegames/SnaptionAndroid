package com.snaptiongame.app.data.providers.api

import android.content.Context

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.snaptiongame.app.BuildConfig
import com.snaptiongame.app.R
import com.snaptiongame.app.data.api.SnaptionApi
import com.snaptiongame.app.data.converters.ActivityFeedItemConverter
import com.snaptiongame.app.data.converters.AddFriendConverter
import com.snaptiongame.app.data.converters.BranchConverter
import com.snaptiongame.app.data.converters.CaptionConverter
import com.snaptiongame.app.data.converters.CaptionSetConverter
import com.snaptiongame.app.data.converters.FitBCaptionConverter
import com.snaptiongame.app.data.converters.FriendConverter
import com.snaptiongame.app.data.converters.GameActionConverter
import com.snaptiongame.app.data.converters.GameConverter
import com.snaptiongame.app.data.converters.OAuthConverter
import com.snaptiongame.app.data.converters.RankConverter
import com.snaptiongame.app.data.converters.SessionConverter
import com.snaptiongame.app.data.converters.UserConverter
import com.snaptiongame.app.data.converters.UserStatsConverter
import com.snaptiongame.app.data.cookies.PersistentCookieStore
import com.snaptiongame.app.data.models.ActivityFeedItem
import com.snaptiongame.app.data.models.AddFriendRequest
import com.snaptiongame.app.data.models.Caption
import com.snaptiongame.app.data.models.CaptionSet
import com.snaptiongame.app.data.models.DeepLinkRequest
import com.snaptiongame.app.data.models.FitBCaption
import com.snaptiongame.app.data.models.Friend
import com.snaptiongame.app.data.models.Game
import com.snaptiongame.app.data.models.GameAction
import com.snaptiongame.app.data.models.OAuthRequest
import com.snaptiongame.app.data.models.Rank
import com.snaptiongame.app.data.models.Session
import com.snaptiongame.app.data.models.User
import com.snaptiongame.app.data.models.UserStats

import java.io.IOException
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit

import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

import io.reactivex.schedulers.Schedulers
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

/**
 * The Snaption API Provider provides an instance of
 * the API service built by Retrofit.
 *
 * @author Tyler Wong
 * @version 1.0
 */
object ApiProvider {
    private var apiService: SnaptionApi? = null
    private var cookieStore: PersistentCookieStore? = null
    private var trustManager: X509TrustManager? = null

    @JvmStatic
    var gson: Gson? = null
        private set

    private const val CERT_TYPE = "X.509"
    private const val CA = "ca"
    private const val TLS = "TLS"
    private const val CONNECTION_TIMEOUT: Long = 60

    /**
     * This method creates the Snaption API service.
     *
     * @return An instance of a Game API service
     */
    @JvmStatic
    fun init(context: Context?) {
        if (apiService == null) {
            synchronized(ApiProvider.javaClass) {
                gson = setupGson()

                apiService = Retrofit.Builder()
                        .baseUrl(BuildConfig.SERVER_ENDPOINT)
                        .client(makeOkHttpClient(context))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                        .addConverterFactory(GsonConverterFactory.create(gson!!))
                        .build()
                        .create(SnaptionApi::class.java)
            }
        }
    }

    /**
     * This method provides the Snaption API service.
     *
     * @return An instance of a Game API service
     */
    @JvmStatic
    fun getApiService(): SnaptionApi {
        return apiService!!
    }

    /**
     * This method provides and handles the creation of an OkHttpClient.
     * If we are an a debug build, add a logging interceptor,
     * otherwise provide no logging interceptor.
     *
     * @return The development or production OkHttpClient
     */
    private fun makeOkHttpClient(context: Context?): OkHttpClient {
        cookieStore = PersistentCookieStore(context)
        val cookieHandler = CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL)
        CookieHandler.setDefault(cookieHandler)
        val cookieJar = JavaNetCookieJar(cookieHandler)
        val socketFactory: SSLSocketFactory
        val okHttpClientBuilder = OkHttpClient.Builder()
                .readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)

        try {
            okHttpClientBuilder.cookieJar(cookieJar)

            if (BuildConfig.DEBUG) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                okHttpClientBuilder.addInterceptor(interceptor)
                socketFactory = getSSLConfig(context, R.raw.api_cert_dev)
                        .socketFactory
                okHttpClientBuilder.hostnameVerifier { _, _ -> true }
            }
            else {
                socketFactory = getSSLConfig(context, R.raw.api_cert_prod)
                        .socketFactory
            }
            okHttpClientBuilder.sslSocketFactory(socketFactory, trustManager!!)
        }
        catch (e: CertificateException) {
            Timber.e("Could not initialize OkHttpClient with SSL Certificate", e)
        }
        catch (e: KeyStoreException) {
            Timber.e("Could not initialize OkHttpClient with SSL Certificate", e)
        }
        catch (e: NoSuchAlgorithmException) {
            Timber.e("Could not initialize OkHttpClient with SSL Certificate", e)
        }
        catch (e: KeyManagementException) {
            Timber.e("Could not initialize OkHttpClient with SSL Certificate", e)
        }
        catch (e: IOException) {
            Timber.e("Could not initialize OkHttpClient with SSL Certificate", e)
        }
        finally {
            okHttpClientBuilder.cookieJar(cookieJar)
        }

        return okHttpClientBuilder.build()
    }

    @JvmStatic
    fun clearCookies() {
        cookieStore?.removeAll()
    }

    /**
     * This method will open the .crt file in the raw resource folder
     * and create the necessary objects to correctly provide a secure
     * connection between our application and the server.
     *
     * @return A new instance of an SSLContext
     * @throws CertificateException if the certificate could not be found
     * @throws KeyStoreException if the keystore could not be found
     * @throws NoSuchAlgorithmException if the algorithm could not be found
     * @throws KeyManagementException if we could not verify the keystore
     * @throws IOException if we could not verify the certificate
     */
    @Throws(CertificateException::class, KeyStoreException::class, NoSuchAlgorithmException::class, KeyManagementException::class, IOException::class)
    private fun getSSLConfig(context: Context?, certResourceId: Int): SSLContext {

        val certificateFactory: CertificateFactory = CertificateFactory.getInstance(CERT_TYPE)

        // Open certificate from raw resource
        var certificate: Certificate? = null
        context?.resources?.openRawResource(certResourceId).use { cert ->
            certificate = certificateFactory.generateCertificate(cert)
        }

        // Creating a KeyStore containing our trusted CAs
        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(null, null)
        keyStore.setCertificateEntry(CA, certificate)

        // Creating a TrustManager that trusts the CAs in our KeyStore.
        val trustManagerAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
        val trustManagerFactory = TrustManagerFactory.getInstance(trustManagerAlgorithm)
        trustManagerFactory.init(keyStore)

        // Find correct X509TrustManager
        val trustManagers = trustManagerFactory.trustManagers
        trustManagers.forEach { manager ->
            if (manager is X509TrustManager) {
                trustManager = manager
            }
        }

        // Creating an SSLSocketFactory that uses our TrustManager
        val sslContext = SSLContext.getInstance(TLS)
        sslContext.init(null, trustManagerFactory.trustManagers, null)

        return sslContext
    }

    /**
     * This method provides and handles the creation of a Gson parser.
     * It will add all of the necessary Type Adapters to serialize and
     * deserialize objects passed to and from the server.
     *
     * @return The Gson parser to be used with a Retrofit instance
     */
    private fun setupGson(): Gson {
        val builder = GsonBuilder()
        builder.registerTypeAdapter(OAuthRequest::class.java, OAuthConverter())
        builder.registerTypeAdapter(Session::class.java, SessionConverter())
        builder.registerTypeAdapter(User::class.java, UserConverter())
        builder.registerTypeAdapter(Game::class.java, GameConverter())
        builder.registerTypeAdapter(Caption::class.java, CaptionConverter())
        builder.registerTypeAdapter(GameAction::class.java, GameActionConverter())
        builder.registerTypeAdapter(Friend::class.java, FriendConverter())
        builder.registerTypeAdapter(AddFriendRequest::class.java, AddFriendConverter())
        builder.registerTypeAdapter(CaptionSet::class.java, CaptionSetConverter())
        builder.registerTypeAdapter(FitBCaption::class.java, FitBCaptionConverter())
        builder.registerTypeAdapter(DeepLinkRequest::class.java, BranchConverter())
        builder.registerTypeAdapter(Rank::class.java, RankConverter())
        builder.registerTypeAdapter(UserStats::class.java, UserStatsConverter())
        builder.registerTypeAdapter(ActivityFeedItem::class.java, ActivityFeedItemConverter())
        builder.excludeFieldsWithoutExposeAnnotation()
        return builder.create()
    }
}
