package ru.mvrlrd.mytranslator.model.datasource.retrofit

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mvrlrd.mytranslator.model.SearchResult
import ru.mvrlrd.mytranslator.model.datasource.DataSource

class ApiHelper(private val apiService: ISkyengApiService) : DataSource<List<SearchResult>>{

    override suspend fun getData(word : String): Response<List<SearchResult>> {
        return apiService.search(word)
    }

}