package ru.mvrlrd.mytranslator.data.local

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardTagCrossRef
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardWithTag
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithWords
import ru.mvrlrd.mytranslator.functional.Either

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

    override suspend fun getAllTags(): Either<Failure, List<Category>> {
        return Either.Right(historyDao.getAllTags())
    }


    override suspend fun assignTagToCard(
        cardId: Long,
        tagId: Long
    ): Either<Failure,  Long> {
        return Either.Right(historyDao.insertCardTagCrossRef(CardTagCrossRef(cardId,tagId)))
    }

    override suspend fun deleteTagFromCard(
        cardId: Long,
        tagId: Long
    ): Either<Failure, Int> {
        return Either.Right(historyDao.removeAssignedTag(cardId, tagId))
    }

    override suspend fun insertNewTagToDb(name: String, icon : String): Either<Failure, Long> {
        return Either.Right(historyDao.insertNewTagToDb(Category(0,name = name, icon = icon )))
    }

    override suspend fun getTagsOfCurrentCard(cardId: Long): Either<Failure, CardWithTag> {
        return Either.Right(historyDao.getTagsOfCard(cardId))
    }

    override suspend fun clearCategories(): Either<Failure, Int> {
        return Either.Right(historyDao.clearCategories())
    }


    override suspend fun getCardsOfCategory(categoryId: Long): Either<Failure, CategoryWithWords> {
        return Either.Right(historyDao.getCardsOfCategory(categoryId))
    }
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