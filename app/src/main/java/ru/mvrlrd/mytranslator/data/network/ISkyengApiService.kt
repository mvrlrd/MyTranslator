package ru.mvrlrd.mytranslator.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.mvrlrd.mytranslator.data.network.response.ListSearchResult

interface ISkyengApiService {

    @GET("words/search")
    suspend fun search(@Query("search") wordToSearch: String): Response<ListSearchResult?>
}