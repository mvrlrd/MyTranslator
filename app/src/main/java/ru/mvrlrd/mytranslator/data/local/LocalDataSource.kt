package ru.mvrlrd.mytranslator.data.local

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardWithTag
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithWords
import ru.mvrlrd.mytranslator.functional.Either

interface LocalDataSource {
    //words
    suspend fun saveCardToDb(cardOfWord: CardOfWord): Either<Failure, Long>
    suspend fun deleteCardFromDb(id: Long): Either<Failure, Int>
    suspend fun clearAllCardsFromDb():Either<Failure, Int>
    suspend fun getAllCardsFromDb(): Either<Failure, List<CardOfWord>>

    //category
    suspend fun insertNewTagToDb(category: Category): Either<Failure, Long>
    suspend fun deleteCategory(categoryId: Long): Either<Failure, Int>
    suspend fun clearCategories(): Either<Failure, Int>
    suspend fun getAllCategoriesForLearning(): Either<Failure, List<Category>>
    suspend fun getAllTags(): Either<Failure, List<Category>>

    //crossref
    suspend fun assignTagToCard(cardId: Long, tagId: Long): Either<Failure, Long>
    suspend fun deleteTagFromCard(cardId: Long, tagId: Long): Either<Failure, Int>
    suspend fun getCardsOfCategory(categoryId: Long): Either<Failure, CategoryWithWords>

    //garbage
    suspend fun getTagsOfCurrentCard(cardId: Long): Either<Failure, CardWithTag>
}