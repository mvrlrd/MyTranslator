package ru.mvrlrd.mytranslator.data

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.LocalDataSource
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithCards
import ru.mvrlrd.mytranslator.data.network.response.ListSearchResult
import ru.mvrlrd.mytranslator.domain.ILocalRepository
import ru.mvrlrd.mytranslator.domain.IRepository
import ru.mvrlrd.mytranslator.functional.Either

class LocalIRepository (private val localDataSource: LocalDataSource
) : ILocalRepository {

    //local
    override suspend fun saveCardToDb(card: Card): Either<Failure, Long> {
        return localDataSource.insertCardToDb(card)
    }

    override suspend fun deleteCardFromDb(id: Long): Either<Failure, Int> {
        return localDataSource.deleteCardFromDb(id)
    }

    override suspend fun clearAllCardsFromDb(): Either<Failure, Int> {
        return localDataSource.clearAllCardsFromDb()
    }

    override suspend fun getAllCardsFromDb(): Either<Failure, List<Card>> {
        return localDataSource.getAllCardsOfDb()
    }

    //category
    override suspend fun addCategoryToDb(category: Category): Either<Failure, Long> {
        return localDataSource.insertCategoryToDb(category)
    }

    override suspend fun deleteCategoryFromDb(categoryId: Long): Either<Failure, Int> {
        return localDataSource.deleteCategoryFromDb(categoryId)
    }

    override suspend fun clearAllCategoriesFromDb(): Either<Failure, Int> {
        return localDataSource.clearAllCategoriesFromDb()
    }

    override suspend fun getAllCategoriesForLearning(): Either<Failure, List<Category>> {
        return localDataSource.getAllCategoriesForLearning()
    }

    override suspend fun getAllCategories(): Either<Failure, List<Category>> {
        return localDataSource.getAllCategoriesOfDb()
    }

    //crossref
    override suspend fun assignCardToCategory(cardId: Long, tagId: Long): Either<Failure, Long> {
        return localDataSource.assignCardToCategory(cardId, tagId)
    }

    override suspend fun deleteCardFromCategory(cardId: Long, tagId: Long): Either<Failure, Int> {
        return localDataSource.deleteCardFromCategory(cardId, tagId)
    }

    override suspend fun getCardsOfCategory(categoryId: Long): Either<Failure, CategoryWithCards> {
        return localDataSource.getCardsOfCategory(categoryId)
    }
}