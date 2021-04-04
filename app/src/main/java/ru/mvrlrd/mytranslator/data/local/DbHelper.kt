package ru.mvrlrd.mytranslator.data.local

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardTagCrossRef
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardWithTag
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithWords
import ru.mvrlrd.mytranslator.functional.Either

class DbHelper(private val historyDao: HistoryDao) : LocalDataSource {

    //words
    override suspend fun saveCardToDb(cardOfWord: CardOfWord): Either<Failure, Long> {
        return Either.Right(historyDao.insert(cardOfWord))
    }

    override suspend fun deleteCardFromDb(id: Long): Either<Failure, Int> {
        return Either.Right(historyDao.delete(id))
    }

    override suspend fun clearAllCardsFromDb(): Either<Failure, Int> {
        return Either.Right(historyDao.clear())
    }

    override suspend fun getAllCardsFromDb(): Either<Failure, List<CardOfWord>> {
        return Either.Right(historyDao.getAll())
    }

    //category
    override suspend fun insertNewTagToDb(category: Category): Either<Failure, Long> {
        return Either.Right(historyDao.insertNewTagToDb(category))
    }

    override suspend fun deleteCategory(categoryId: Long): Either<Failure, Int> {
        return Either.Right(historyDao.deleteCategory(categoryId))
    }

    override suspend fun clearCategories(): Either<Failure, Int> {
        return Either.Right(historyDao.clearCategories())
    }
    override suspend fun getAllCategoriesForLearning(): Either<Failure, List<Category>> {
        return Either.Right(historyDao.getAllCategoriesForLearning())
    }

    override suspend fun getAllTags(): Either<Failure, List<Category>> {
        return Either.Right(historyDao.getAllTags())
    }

    //crossref
    override suspend fun assignTagToCard(
        cardId: Long,
        tagId: Long
    ): Either<Failure, Long> {
        return Either.Right(historyDao.insertCardTagCrossRef(CardTagCrossRef(cardId, tagId)))
    }

    override suspend fun deleteTagFromCard(
        cardId: Long,
        tagId: Long
    ): Either<Failure, Int> {
        return Either.Right(historyDao.removeAssignedTag(cardId, tagId))
    }

    override suspend fun getCardsOfCategory(categoryId: Long): Either<Failure, CategoryWithWords> {
        return Either.Right(historyDao.getCardsOfCategory(categoryId))
    }

    //garbage
    override suspend fun getTagsOfCurrentCard(cardId: Long): Either<Failure, CardWithTag> {
        return Either.Right(historyDao.getTagsOfCard(cardId))
    }
}

