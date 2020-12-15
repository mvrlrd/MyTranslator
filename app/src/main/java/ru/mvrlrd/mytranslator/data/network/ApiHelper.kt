package ru.mvrlrd.mytranslator.data.data.network

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.response.SearchResult
import ru.mvrlrd.mytranslator.functional.Either

class ApiHelper(private val apiService: ISkyengApiService) :
    DataSource {

    override suspend fun getData(wordForTranslation : String): Either<Failure, SearchResult?> {
        return apiService.search(wordForTranslation)
    }

}