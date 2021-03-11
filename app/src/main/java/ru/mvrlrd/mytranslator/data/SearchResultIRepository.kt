package ru.mvrlrd.mytranslator.data

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.LocalDataSource
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.data.network.RemoteDataSource
import ru.mvrlrd.mytranslator.data.network.response.ListSearchResult
import ru.mvrlrd.mytranslator.domain.IRepository
import ru.mvrlrd.mytranslator.functional.Either
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler

class SearchResultIRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IRepository {

    override suspend fun getSearchResult(wordForTranslation: String): Either<Failure, ListSearchResult?> {
        return remoteDataSource.getData(wordForTranslation)
    }

    override suspend fun getAllCardsFromDb(): Either<Failure, List<CardOfWord>> {
        return localDataSource.getAllCardsFromDb()
    }

    override suspend fun saveCardToDb(cardOfWord: CardOfWord): Either<Failure, Long> {
        return localDataSource.saveCardToDb(cardOfWord)
    }

    override suspend fun deleteCardFromDb(id: Long): Either<Failure, Int> {
        return localDataSource.deleteCardFromDb(id)
    }
//
//    override suspend fun assignTagToCard(
//        cardId: Long,
//        tagId: Long
//    ): Either<Failure, Map<Long, Long>> {
//        return localDataSource.assignTagToCard(cardId,tagId)
//    }
//
//    override suspend fun addNewTag(tag: String): Either<Failure, Long> {
//        return localDataSource.
//    }
//
//    override suspend fun deleteTagFromCard(
//        cardId: Long,
//        tagId: Long
//    ) {
//         localDataSource.deleteTagFromCard(cardId, tagId)
//    }
}