package ru.mvrlrd.mytranslator.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.mvrlrd.mytranslator.data.local.entity.GroupTag
import ru.mvrlrd.mytranslator.data.local.entity.HistoryEntity
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardTagCrossRef

@Database(entities = [HistoryEntity::class, GroupTag::class, CardTagCrossRef::class], version = 4)
abstract class AppSearchingHistoryDataBase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}