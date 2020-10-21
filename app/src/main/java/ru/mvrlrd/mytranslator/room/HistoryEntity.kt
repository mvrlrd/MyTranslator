package ru.mvrlrd.mytranslator.room


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "searching_history")
class HistoryEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo val text: String?,
    @ColumnInfo var translation: String?
)
