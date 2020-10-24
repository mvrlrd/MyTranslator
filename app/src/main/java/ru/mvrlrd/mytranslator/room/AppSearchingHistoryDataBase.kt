package ru.mvrlrd.mytranslator.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class], version = 2)
abstract class AppSearchingHistoryDataBase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}