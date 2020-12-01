package ru.mvrlrd.mytranslator.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Single

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(historyEntity: HistoryEntity?): Long

    @Query("DELETE FROM searching_history")
    suspend fun clear()

    @Query("SELECT * FROM searching_history")
    suspend fun getAll(): List<HistoryEntity>

    @Query("SELECT * FROM searching_history WHERE text=:word")
    suspend fun getCertainWord(word: String): HistoryEntity


}