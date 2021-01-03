package ru.mvrlrd.mytranslator.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.mvrlrd.mytranslator.data.local.entity.HistoryEntity

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(historyEntity: HistoryEntity?): Long

    @Query("DELETE FROM searching_history")
    suspend fun clear()

    @Query("DELETE FROM searching_history WHERE id =:id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM searching_history")
    suspend fun getAll(): List<HistoryEntity>

    @Query("SELECT * FROM searching_history WHERE text=:word")
    suspend fun getCertainWord(word: String): HistoryEntity


}