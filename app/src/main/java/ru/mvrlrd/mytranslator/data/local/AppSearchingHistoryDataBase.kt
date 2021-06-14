package ru.mvrlrd.mytranslator.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardCategoryCrossRef

@Database(entities = [Card::class, Category::class, CardCategoryCrossRef::class], version = 11)
abstract class AppSearchingHistoryDataBase : RoomDatabase() {
    abstract fun allDatabasesDao(): AllDatabasesDao
}