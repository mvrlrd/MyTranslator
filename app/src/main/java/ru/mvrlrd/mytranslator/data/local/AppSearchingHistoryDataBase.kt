package ru.mvrlrd.mytranslator.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.mvrlrd.mytranslator.data.local.entity.HistoryEntity

@Database(entities = [HistoryEntity::class], version = 2)
abstract class AppSearchingHistoryDataBase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}