package ru.mvrlrd.mytranslator.data.local

import androidx.room.*
import ru.mvrlrd.mytranslator.data.local.entity.GroupTag
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardTagCrossRef
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardWithTag

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cardOfWord: CardOfWord?): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewTagToDb(tag: GroupTag): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCardTagCrossRef(cardTagCrossRef: CardTagCrossRef):Long


    @Query("DELETE FROM searching_history")
    suspend fun clear()

    @Query("DELETE FROM group_tags")
    suspend fun clearTags()

    @Query("DELETE FROM searching_history WHERE id =:id")
    suspend fun delete(id: Long): Int

    @Query("SELECT * FROM searching_history")
    suspend fun getAll(): List<CardOfWord>

    @Query("SELECT * FROM group_tags")
    suspend fun getAllTags(): List<GroupTag>

    @Query("SELECT * FROM searching_history WHERE text=:word")
    suspend fun getCertainWord(word: String): CardOfWord


    @Query("SELECT * FROM cardtagcrossref")
    suspend fun getAllCrossRef(): List<CardTagCrossRef>




    @Query("DELETE  FROM cardtagcrossref WHERE id =:id AND tagId =:tagID")
    suspend fun removeAssignedTag(id : Long, tagID : Long) : Int



    @Transaction
    @Query("SELECT * FROM searching_history WHERE id = :id")
    suspend fun getTagsOfCard(id: Long): CardWithTag

    @Transaction
    @Query("SELECT * FROM searching_history WHERE id = :tagId")
    suspend fun getCardsOfTag(tagId: Long): List<CardWithTag>

}