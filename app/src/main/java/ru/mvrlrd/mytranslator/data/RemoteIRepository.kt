package ru.mvrlrd.mytranslator.data

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.LocalDataSource
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithCards
import ru.mvrlrd.mytranslator.data.network.RemoteDataSource
import ru.mvrlrd.mytranslator.data.network.response.ListSearchResult
import ru.mvrlrd.mytranslator.domain.IRepository
import ru.mvrlrd.mytranslator.functional.Either

class RemoteIRepository(
    private val remoteDataSource: RemoteDataSource

) : IRepository {

    //words
    //remote
    override suspend fun getSearchResult(wordForTranslation: String): Either<Failure, ListSearchResult?> {
        return remoteDataSource.getData(wordForTranslation)
    }


}