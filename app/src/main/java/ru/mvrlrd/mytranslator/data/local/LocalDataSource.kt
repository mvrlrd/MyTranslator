package ru.mvrlrd.mytranslator.data.local

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardWithTag
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithWords
import ru.mvrlrd.mytranslator.functional.Either

interface LocalDataSource {
    suspend fun getAllCardsFromDb() : Either<Failure, List<CardOfWord>>

    suspend fun saveCardToDb(cardOfWord : CardOfWord) : Either<Failure, Long>

    suspend fun deleteCardFromDb(id : Long) : Either<Failure, Int>

    suspend fun getAllTags() : Either<Failure, List<Category>>

    suspend fun assignTagToCard(cardId : Long, tagId : Long) : Either<Failure, Long>

    suspend fun deleteTagFromCard(cardId : Long, tagId : Long) : Either<Failure, Int>

    suspend fun insertNewTagToDb(name: String, icon : String):Either<Failure,Long>

    suspend fun getTagsOfCurrentCard(cardId: Long): Either<Failure, CardWithTag>

    suspend fun clearCategories(): Either<Failure, Int>

    suspend fun getCardsOfCategory(categoryId:Long):Either<Failure,CategoryWithWords>
}