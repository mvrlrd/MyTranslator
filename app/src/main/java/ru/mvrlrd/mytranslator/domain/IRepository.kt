package ru.mvrlrd.mytranslator.domain

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardWithTag
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithWords
import ru.mvrlrd.mytranslator.data.network.response.ListSearchResult
import ru.mvrlrd.mytranslator.functional.Either

interface IRepository {
    suspend fun getSearchResult(
        wordForTranslation: String
    ): Either<Failure, ListSearchResult?>


    suspend fun getAllCardsFromDb() : Either<Failure, List<CardOfWord>>

    suspend fun saveCardToDb(cardOfWord: CardOfWord) : Either<Failure, Long>

    suspend fun deleteCardFromDb(id : Long) : Either<Failure, Int>

    suspend fun getAllTags() : Either<Failure, List<Category>>

    suspend fun addNewTagToDb(name: String, icon : String): Either<Failure, Long>

    suspend fun assignTagToCard(cardId : Long, tagId : Long) : Either<Failure, Long>

    suspend fun deleteTagFromCard(cardId : Long, tagId : Long): Either<Failure, Int>

    suspend fun getTagsOfCurrentCard(cardId: Long): Either<Failure, CardWithTag>

    suspend fun clearCategoriesFromDb(): Either<Failure, Int>


    suspend fun getCardsOfCategory(categoryId: Long): Either<Failure, CategoryWithWords>

}