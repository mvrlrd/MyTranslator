package ru.mvrlrd.mytranslator.data.local.entity


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "searching_history")
data class HistoryEntity (
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo val text: String?,
    @ColumnInfo var translation: String?,
    @ColumnInfo val image_url: String?,
    @ColumnInfo val transcription: String?,
    @ColumnInfo val partOfSpeech: String?,
    @ColumnInfo val prefix: String?
):Parcelable{
//    @Ignore
//    constructor(text:String?,translation: String? ): this(0,text,translation)
}
