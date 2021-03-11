package ru.mvrlrd.mytranslator.data.local

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.functional.Either

interface LocalDataSource {
    suspend fun getAllCardsFromDb() : Either<Failure, List<CardOfWord>>

    suspend fun saveCardToDb(cardOfWord : CardOfWord) : Either<Failure, Long>

    suspend fun deleteCardFromDb(id : Long) : Either<Failure, Int>
//
//    suspend fun assignTagToCard(cardId : Long, tagId : Long) : Either<Failure, Map<Long,Long>>
//
//    suspend fun deleteTagFromCard(cardId : Long, tagId : Long)
//
//    suspend fun insertNewTag(tag : String):Either<Failure,Long>
}