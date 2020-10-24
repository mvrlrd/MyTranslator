package ru.mvrlrd.mytranslator.room


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "searching_history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo val text: String?,
    @ColumnInfo var translation: String?

){
    @Ignore
    constructor(text:String?,translation: String? ): this(0,text,translation)
}
