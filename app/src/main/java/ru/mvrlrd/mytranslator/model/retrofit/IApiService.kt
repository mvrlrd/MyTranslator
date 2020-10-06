package ru.mvrlrd.mytranslator.model.retrofit

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import ru.mvrlrd.mytranslator.model.SearchResult

interface IApiService {

    @GET("words/search")
    fun search(@Query("search") wordToSearch: String): Observable<List<SearchResult>>
}