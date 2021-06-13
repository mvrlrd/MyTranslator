package ru.mvrlrd.mytranslator.di.modules

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mvrlrd.mytranslator.BuildConfig
import ru.mvrlrd.mytranslator.data.network.ApiHelper
import ru.mvrlrd.mytranslator.data.network.ISkyengApiService
import java.util.concurrent.TimeUnit

internal const val BASE_URL = "https://dictionary.skyeng.ru/api/public/v1/"
private const val TIMEOUT_IN_SECONDS = 1L

internal fun provideOkHTTPClient(): OkHttpClient {
    val requestInterceptor = Interceptor { chain ->
        val httpUrl = chain.request()
            .url
            .newBuilder()
            .build()
        val request = chain.request()
            .newBuilder()
            .url(httpUrl)
            .build()
        return@Interceptor chain.proceed(request)
    }
    return if (BuildConfig.DEBUG) {
        OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .build()
    } else {
        OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .build()
    }
}

internal fun provideRetrofit(
    okHttpClient: OkHttpClient,
    BASE_URL: String
): Retrofit = Retrofit.Builder().baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(okHttpClient)
    .build()

internal fun provideApiService(retrofit: Retrofit): ISkyengApiService =
    retrofit.create(ISkyengApiService::class.java)

val retrofitModule = module {
    single { provideOkHTTPClient() }
    single { provideRetrofit(get(), BASE_URL) }
    single { provideApiService(get()) }
    single { ApiHelper(get(), get()) }
}