package ru.mvrlrd.mytranslator.data.local

import retrofit2.Response
import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.data.local.entity.GroupTag
import ru.mvrlrd.mytranslator.data.network.response.ListSearchResult
import ru.mvrlrd.mytranslator.functional.Either
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler

class DbHelper(private val historyDao: HistoryDao) : LocalDataSource {

    override suspend fun getAllCardsFromDb(): Either<Failure, List<CardOfWord>> {
        return Either.Right(historyDao.getAll())
    }

    override suspend fun saveCardToDb(cardOfWord: CardOfWord): Either<Failure, Long> {
        return Either.Right(historyDao.insert(cardOfWord))
    }

    override suspend fun deleteCardFromDb(id: Long): Either<Failure, Int> {
        return Either.Right(historyDao.delete(id))
    }
//
//    override suspend fun assignTagToCard(
//        cardId: Long,
//        tagId: Long
//    ): Either<Failure, Map<Long, Long>> {
//        return Either.Right(historyDao.)
//    }
//
//    override suspend fun deleteTagFromCard(
//        cardId: Long,
//        tagId: Long
//    ) {
//         Either.Right(historyDao.removeAssignedTag(cardId, tagId))
//    }
//
//    override suspend fun insertNewTag(tag: String): Either<Failure, Long> {
//        return Either.Right(historyDao.insertTag(GroupTag(0,tag = tag, isChecked = false)))
//    }
}

//override suspend fun getData(wordForTranslation : String): Either<Failure, ListSearchResult?> =
//    requestData { apiService.search(wordForTranslation)}
//
//
//
//private fun <T> responseHandle(response: Response<T>): Either<Failure, T?> =
//    when (response.isSuccessful) {
//        true -> Either.Right(response.body())
//        false -> Either.Left(Failure.ServerError)
//    }
//
//private suspend fun <T> requestData(request: suspend () -> Response<T>) =
//    when (networkHandler.isConnected()) {
//        true -> {
//            try {
//                responseHandle(request())
//            } catch (exception: Throwable) {
//                Either.Left(Failure.ServerError)
//            }
//        }
//        false -> Either.Left(Failure.NetworkConnection)
//    }