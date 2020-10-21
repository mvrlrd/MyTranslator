package ru.mvrlrd.mytranslator.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Single

@Dao
interface HistoryDao {
    @Insert
    fun insert(historyEntity: HistoryEntity?): Single<Long?>?
    @Query("DELETE FROM searching_history")
    fun clear()

}