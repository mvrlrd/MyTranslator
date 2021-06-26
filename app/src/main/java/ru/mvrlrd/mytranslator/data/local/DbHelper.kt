package ru.mvrlrd.mytranslator.data.local

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardCategoryCrossRef
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithCards
import ru.mvrlrd.mytranslator.functional.Either

class DbHelper(private val allDatabasesDao: AllDatabasesDao) : LocalDataSource {



    override fun getAllCatsFlow(): Flow<List<Category>> {
        return allDatabasesDao.getAllCatsFlow()
    }

    //words
    override suspend fun insertCardToDb(card: Card): Either<Failure, Long> {
        return Either.Right(allDatabasesDao.insertCard(card))
    }

    override suspend fun deleteCardFromDb(id: Long): Either<Failure, Int> {
        return Either.Right(allDatabasesDao.deleteCard(id))
    }

    override suspend fun clearAllCardsFromDb(): Either<Failure, Int> {
        return Either.Right(allDatabasesDao.clearCards())
    }

    override suspend fun getAllCardsOfDb(): Either<Failure, List<Card>> {
        return Either.Right(allDatabasesDao.getAllCards())
    }

    override suspend fun updateCardProgress(cardId: Long, newProgress: Int): Either<Failure, Int> {
        return Either.Right(allDatabasesDao.updateCardProgress(cardId,newProgress))
    }

    //category
    override suspend fun insertCategoryToDb(category: Category): Either<Failure, Long> {
        return Either.Right(allDatabasesDao.insertCategory(category))
    }

    override suspend fun deleteCategoryFromDb(categoryId: Long): Either<Failure, Int> {
        return Either.Right(allDatabasesDao.deleteCategory(categoryId))
    }

    override suspend fun clearAllCategoriesFromDb(): Either<Failure, Int> {
        return Either.Right(allDatabasesDao.clearCategories())
    }

    override suspend fun getAllCategoriesForLearning(): Either<Failure, List<Category>> {
        return Either.Right(allDatabasesDao.getCategoriesForLearning())
    }

    override suspend fun getAllCategoriesOfDb(): Either<Failure, List<Category>> {
        return Either.Right(allDatabasesDao.getAllCategories())
    }

    override suspend fun updateCategoryProgress(
        categoryId: Long,
        newProgress: Double
    ): Either<Failure, Int> {
        return Either.Right(allDatabasesDao.updateCategoryProgress(categoryId, newProgress))
    }

    override suspend fun updateCategory(
        categoryId: Long,
        newName: String,
        newIcon: String
    ): Either<Failure, Int> {
        return Either.Right(allDatabasesDao.updateCategory(categoryId, newName, newIcon))
    }

    override suspend fun updateCategoryIsChecked(
        categoryId: Long,
        isChecked: Boolean
    ): Either<Failure, Int> {
        return Either.Right(allDatabasesDao.updateCategoryIsChecked(categoryId, isChecked))
    }

    //crossref
    override suspend fun assignCardToCategory(cardId: Long, tagId: Long): Either<Failure, Long> {
        return Either.Right(
            allDatabasesDao.insertCardToCategory(
                CardCategoryCrossRef(
                    cardId,
                    tagId
                )
            )
        )
    }

    override suspend fun deleteCardFromCategory(
        cardId: Long, tagId: Long
    ): Either<Failure, Int> {
        return Either.Right(allDatabasesDao.removeCardFromCategory(cardId, tagId))
    }

    override suspend fun getCardsOfCategory(categoryId: Long): Either<Failure, CategoryWithCards> {
        return Either.Right(allDatabasesDao.getCardsOfCategory(categoryId))
    }
}

