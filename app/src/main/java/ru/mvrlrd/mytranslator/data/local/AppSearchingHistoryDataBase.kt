package ru.mvrlrd.mytranslator.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.mvrlrd.mytranslator.data.local.entity.GroupTag
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardTagCrossRef

@Database(entities = [CardOfWord::class, GroupTag::class, CardTagCrossRef::class], version = 4)
abstract class AppSearchingHistoryDataBase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}