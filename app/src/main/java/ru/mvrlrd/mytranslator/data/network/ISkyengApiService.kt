package ru.mvrlrd.mytranslator.data.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.mvrlrd.mytranslator.data.response.ListSearchResult

interface ISkyengApiService {

    @GET("words/search")
    suspend fun search(@Query("search") wordToSearch: String): Response<ListSearchResult?>
}