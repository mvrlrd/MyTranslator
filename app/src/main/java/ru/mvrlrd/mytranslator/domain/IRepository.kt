package ru.mvrlrd.mytranslator.domain

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.data.local.entity.GroupTag
import ru.mvrlrd.mytranslator.data.network.response.ListSearchResult
import ru.mvrlrd.mytranslator.functional.Either
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler

interface IRepository {
    suspend fun getSearchResult(
        wordForTranslation: String
    ): Either<Failure, ListSearchResult?>


    suspend fun getAllCardsFromDb() : Either<Failure, List<CardOfWord>>

    suspend fun saveCardToDb(cardOfWord: CardOfWord) : Either<Failure, Long>

//    suspend fun deleteCardFromDb(id : Long) : Either<Failure, Long>
//
//    suspend fun addNewTag(tag : String): Either<Failure, Long>
//
//    suspend fun assignTagToCard(cardId : Long, tagId : Long) : Either<Failure, Map<Long,Long>>
//
//    suspend fun deleteTagFromCard(cardId : Long, tagId : Long)

}