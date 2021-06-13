package ru.mvrlrd.mytranslator.data.local

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardWithCategory
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithCards
import ru.mvrlrd.mytranslator.functional.Either

interface LocalDataSource {
    //words
    suspend fun saveCardToDb(card: Card): Either<Failure, Long>
    suspend fun deleteCardFromDb(id: Long): Either<Failure, Int>
    suspend fun clearAllCardsFromDb():Either<Failure, Int>
    suspend fun getAllCardsFromDb(): Either<Failure, List<Card>>

    //category
    suspend fun insertNewTagToDb(category: Category): Either<Failure, Long>
    suspend fun deleteCategory(categoryId: Long): Either<Failure, Int>
    suspend fun clearCategories(): Either<Failure, Int>
    suspend fun getAllCategoriesForLearning(): Either<Failure, List<Category>>
    suspend fun getAllTags(): Either<Failure, List<Category>>

    //crossref
    suspend fun assignTagToCard(cardId: Long, tagId: Long): Either<Failure, Long>
    suspend fun deleteTagFromCard(cardId: Long, tagId: Long): Either<Failure, Int>
    suspend fun getCardsOfCategory(categoryId: Long): Either<Failure, CategoryWithCards>

    //garbage
    suspend fun getTagsOfCurrentCard(cardId: Long): Either<Failure, CardWithCategory>
}