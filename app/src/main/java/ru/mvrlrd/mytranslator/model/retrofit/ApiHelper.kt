package ru.mvrlrd.mytranslator.model.retrofit

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mvrlrd.mytranslator.model.SearchResult

class ApiHelper {
    fun requestServer(word : String): Observable<List<SearchResult>> {

        val gson = GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
        val gsonConverterFactory = GsonConverterFactory.create(gson)
        val api = Retrofit.Builder()
            .baseUrl(BASIC_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(IApiService::class.java)
        return api.search(word).subscribeOn(Schedulers.io())
    }

    companion object {
        private const val BASIC_URL = "https://dictionary.skyeng.ru/api/public/v1/"
    }
}