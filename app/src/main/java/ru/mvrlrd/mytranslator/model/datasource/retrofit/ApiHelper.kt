package ru.mvrlrd.mytranslator.model.datasource.retrofit

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mvrlrd.mytranslator.model.SearchResult
import ru.mvrlrd.mytranslator.model.datasource.BaseInterceptor
import ru.mvrlrd.mytranslator.model.datasource.DataSource

class ApiHelper : DataSource<List<SearchResult>>{


    override fun getData(word: String): Observable<List<SearchResult>> {
        return getService(BaseInterceptor.interceptor).search(word).subscribeOn(Schedulers.io())
    }

    private fun getService(interceptor: Interceptor): IApiService {
        return createRetrofit(interceptor).create(IApiService::class.java)
    }

    private fun createRetrofit(interceptor: Interceptor): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASIC_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(createOkHttpClient(interceptor))
            .build()
    }

    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return httpClient.build()
    }

//    fun requestServer(word : String): Observable<List<SearchResult>> {
//
//        val gson = GsonBuilder()
//            .create()
//        val gsonConverterFactory = GsonConverterFactory.create(gson)
//        val api = Retrofit.Builder()
//            .baseUrl(BASIC_URL)
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .addConverterFactory(gsonConverterFactory)
//            .build()
//            .create(IApiService::class.java)
//        return api.search(word).subscribeOn(Schedulers.io())
//    }

    companion object {
        private const val BASIC_URL = "https://dictionary.skyeng.ru/api/public/v1/"
    }
}