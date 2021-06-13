package ru.mvrlrd.mytranslator.data.local

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardCategoryCrossRef
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithCards
import ru.mvrlrd.mytranslator.functional.Either

class DbHelper(private val allDatabasesDao: AllDatabasesDao) : LocalDataSource {

    //words
    override suspend fun saveCardToDb(card: Card): Either<Failure, Long> {
        return Either.Right(allDatabasesDao.insertCard(card))
    }

    override suspend fun deleteCardFromDb(id: Long): Either<Failure, Int> {
        return Either.Right(allDatabasesDao.deleteCard(id))
    }

    override suspend fun clearAllCardsFromDb(): Either<Failure, Int> {
        return Either.Right(allDatabasesDao.clearCards())
    }

    override suspend fun getAllCardsFromDb(): Either<Failure, List<Card>> {
        return Either.Right(allDatabasesDao.getAllCards())
    }

    //category
    override suspend fun insertNewTagToDb(category: Category): Either<Failure, Long> {
        return Either.Right(allDatabasesDao.insertCategory(category))
    }

    override suspend fun deleteCategory(categoryId: Long): Either<Failure, Int> {
        return Either.Right(allDatabasesDao.deleteCategory(categoryId))
    }

    override suspend fun clearCategories(): Either<Failure, Int> {
        return Either.Right(allDatabasesDao.clearCategories())
    }

    override suspend fun getAllCategoriesForLearning(): Either<Failure, List<Category>> {
        return Either.Right(allDatabasesDao.getCategoriesForLearning())
    }

    override suspend fun getAllTags(): Either<Failure, List<Category>> {
        return Either.Right(allDatabasesDao.getAllCategories())
    }

    //crossref
    override suspend fun assignTagToCard(cardId: Long, tagId: Long): Either<Failure, Long> {
        return Either.Right(
            allDatabasesDao.insertCardToCategory(
                CardCategoryCrossRef(
                    cardId,
                    tagId
                )
            )
        )
    }

    override suspend fun deleteTagFromCard(
        cardId: Long, tagId: Long
    ): Either<Failure, Int> {
        return Either.Right(allDatabasesDao.removeCardFromCategory(cardId, tagId))
    }

    override suspend fun getCardsOfCategory(categoryId: Long): Either<Failure, CategoryWithCards> {
        return Either.Right(allDatabasesDao.getCardsOfCategory(categoryId))
    }
}

