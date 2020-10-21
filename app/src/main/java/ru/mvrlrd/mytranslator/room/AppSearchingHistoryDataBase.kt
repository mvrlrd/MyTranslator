package ru.mvrlrd.mytranslator.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class], version = 1)
abstract class AppSearchingHistoryDataBase : RoomDatabase(), HistoryDao {
    abstract fun historyDao(): HistoryDao
}