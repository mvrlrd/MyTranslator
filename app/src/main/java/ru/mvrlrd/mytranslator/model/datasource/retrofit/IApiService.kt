package ru.mvrlrd.mytranslator.model.datasource.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.mvrlrd.mytranslator.model.SearchResult

interface IApiService {

    @GET("words/search")
    suspend fun search(@Query("search") wordToSearch: String): Response<List<SearchResult>>
}