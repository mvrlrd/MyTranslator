package ru.mvrlrd.mytranslator.data.local

import androidx.room.*
import ru.mvrlrd.mytranslator.data.local.entity.GroupTag
import ru.mvrlrd.mytranslator.data.local.entity.HistoryEntity
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardTagCrossRef
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardWithTag

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historyEntity: HistoryEntity?): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: GroupTag): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCardTagCrossRef(cardTagCrossRef: CardTagCrossRef)


    @Query("DELETE FROM searching_history")
    suspend fun clear()

    @Query("DELETE FROM group_tags")
    suspend fun clearTags()

    @Query("DELETE FROM searching_history WHERE id =:id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM searching_history")
    suspend fun getAll(): List<HistoryEntity>

    @Query("SELECT * FROM group_tags")
    suspend fun getAllTags(): List<GroupTag>

    @Query("SELECT * FROM searching_history WHERE text=:word")
    suspend fun getCertainWord(word: String): HistoryEntity



//
    @Transaction
    @Query("SELECT * FROM searching_history WHERE id = :id")
    suspend fun getTagsOfCard(id: Long): CardWithTag

    @Transaction
    @Query("SELECT * FROM searching_history WHERE id = :tagId")
    suspend fun getCardsOfTag(tagId: Long): List<CardWithTag>

}