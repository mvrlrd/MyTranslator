package ru.mvrlrd.mytranslator.domain

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithCards
import ru.mvrlrd.mytranslator.data.network.response.ListSearchResult
import ru.mvrlrd.mytranslator.functional.Either

interface IRepository {
    suspend fun getSearchResult(
        wordForTranslation: String
    ): Either<Failure, ListSearchResult?>

    //card
    suspend fun saveCardToDb(card: Card): Either<Failure, Long>

    suspend fun deleteCardFromDb(id: Long): Either<Failure, Int>

    suspend fun clearAllCardsFromDb(): Either<Failure, Int>

    suspend fun getAllCardsFromDb(): Either<Failure, List<Card>>

    //category
    suspend fun addCategoryToDb(category: Category): Either<Failure, Long>

    suspend fun deleteCategoryFromDb(categoryId: Long): Either<Failure, Int>

    suspend fun clearAllCategoriesFromDb(): Either<Failure, Int>

    suspend fun getAllCategoriesForLearning(): Either<Failure, List<Category>>

    suspend fun getAllCategories(): Either<Failure, List<Category>>

    //crossref
    suspend fun assignCardToCategory(cardId: Long, tagId: Long): Either<Failure, Long>

    suspend fun deleteCardFromCategory(cardId: Long, tagId: Long): Either<Failure, Int>

    suspend fun getCardsOfCategory(categoryId: Long): Either<Failure, CategoryWithCards>
}