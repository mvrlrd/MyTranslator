package ru.mvrlrd.mytranslator.room


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "searching_history")
data class HistoryEntity (
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo val text: String?,
    @ColumnInfo var translation: String?

):Parcelable{
    @Ignore
    constructor(text:String?,translation: String? ): this(0,text,translation)
}
