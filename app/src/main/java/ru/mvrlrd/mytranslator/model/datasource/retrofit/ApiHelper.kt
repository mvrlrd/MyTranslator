package ru.mvrlrd.mytranslator.model.datasource.retrofit

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mvrlrd.mytranslator.model.ListSearchResult
import ru.mvrlrd.mytranslator.model.SearchResult
import ru.mvrlrd.mytranslator.model.datasource.DataSource

class ApiHelper : DataSource<ListSearchResult>{


    override suspend fun getData(word : String): Response<ListSearchResult> {

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
        return api.search(word)
    }

    companion object {
        private const val BASIC_URL = "https://dictionary.skyeng.ru/api/public/v1/"
    }
}