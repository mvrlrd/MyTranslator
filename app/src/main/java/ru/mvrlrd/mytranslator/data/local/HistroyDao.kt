package ru.mvrlrd.mytranslator.data.local

import androidx.room.*
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardTagCrossRef
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardWithTag
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithWords

@Dao
interface HistoryDao {
    //words also known as cards
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(word: CardOfWord?): Long

    @Query("DELETE FROM searching_history")
    suspend fun clear()

    @Query("DELETE FROM searching_history WHERE id =:id")
    suspend fun delete(id: Long): Int

    @Query("SELECT * FROM searching_history")
    suspend fun getAll(): List<CardOfWord>

    @Query("SELECT * FROM searching_history WHERE text=:word")
    suspend fun getCertainWord(word: String): CardOfWord



 //categories
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewTagToDb(category: Category): Long

    @Query("DELETE FROM group_tags")
    suspend fun clearCategories():Int

    @Query("SELECT * FROM group_tags")
    suspend fun getAllTags(): List<Category>

    @Query("DELETE FROM group_tags WHERE categoryId =:catId")
    suspend fun deleteCategory(catId: Long): Int




// word from category and vice versa
    @Transaction
    @Query("SELECT * FROM searching_history WHERE id = :id")
    suspend fun getTagsOfCard(id: Long): CardWithTag

    @Transaction
    @Query("SELECT * FROM group_tags WHERE categoryId = :categoryId")
    suspend fun getCardsOfCategory(categoryId: Long): CategoryWithWords

    @Query("DELETE  FROM cardtagcrossref WHERE id =:id AND categoryId =:categoryId")
    suspend fun removeAssignedTag(id : Long, categoryId : Long) : Int

    @Transaction
    @Query("SELECT * FROM searching_history WHERE id = :categoryId")
    suspend fun getCardsOfTag(categoryId: Long): List<CardWithTag>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCardTagCrossRef(cardTagCrossRef: CardTagCrossRef):Long

    @Query("SELECT * FROM cardtagcrossref")
    suspend fun getAllCrossRef(): List<CardTagCrossRef>


}