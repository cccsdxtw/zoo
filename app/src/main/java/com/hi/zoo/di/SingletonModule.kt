package com.hi.zoo.di

import android.annotation.SuppressLint
import android.app.Application
import com.hi.zoo.data.DataRepository
import com.hi.zoo.data.local.DataBase
import com.hi.zoo.data.local.LocalDataSource
import com.hi.zoo.data.remote.ApiService
import com.hi.zoo.data.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.net.InetAddress
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.inject.Qualifier
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {

    private fun baseOkHttpBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .dns(
                DnsOverHttps.Builder()
                    .client(getBootstrapClient())
                    .url("https://1.1.1.1/dns-query".toHttpUrl())
                    .bootstrapDnsHosts(
                        InetAddress.getByName("1.1.1.1"),
                        InetAddress.getByName("1.0.0.1"),
                    )
                    .includeIPv6(false)
                    .build()
            )
    }

    private fun getBootstrapClient(): OkHttpClient {
        val cacheFile = File("cacheDir", "okHttpCache")
        val maxSize = 10L * 1024 * 1024
        val appCache = Cache(cacheFile, maxSize)
        return OkHttpClient.Builder().cache(appCache).build()
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CommonOkHttpClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class UnsafeOkHttpClient

    @Provides
    @Singleton
    @CommonOkHttpClient
    fun provideOkHttpClient(): OkHttpClient {
        return baseOkHttpBuilder().build()
    }

    @Provides
    @Singleton
    @UnsafeOkHttpClient
    @SuppressLint("TrustAllX509TrustManager")
    fun provideUnsafeOkHttpClient(): OkHttpClient {
        return try {
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            val sslSocketFactory = sslContext.socketFactory
            baseOkHttpBuilder()
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }
                .build()
        } catch (e: Exception) {
            e.printStackTrace()
            baseOkHttpBuilder().build()
        }
    }

    @Provides
    @Singleton
    fun provideApiService(@UnsafeOkHttpClient okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(ApiService.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDataBase(application: Application): DataBase {
        return DataBase.build(application)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService: ApiService): RemoteDataSource {
        return RemoteDataSource(apiService)
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(dataBase: DataBase): LocalDataSource {
        return LocalDataSource(dataBase)
    }

    @Provides
    @Singleton
    fun provideDataRepository(
        application: Application,
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
    ): DataRepository {
        return DataRepository(application, remoteDataSource, localDataSource)
    }
}