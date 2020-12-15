package ru.mvrlrd.mytranslator.data

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.data.network.ApiHelper
import ru.mvrlrd.mytranslator.data.data.network.DataSource
import ru.mvrlrd.mytranslator.data.response.SearchResult
import ru.mvrlrd.mytranslator.domain.Repository
import ru.mvrlrd.mytranslator.functional.Either

class SearchResultRepository(private val remoteDataSource: DataSource) : Repository {

    override suspend fun getSearchResult(wordForTranslation: String): Either<Failure, SearchResult?> {
        return remoteDataSource.getData(wordForTranslation)
    }
}