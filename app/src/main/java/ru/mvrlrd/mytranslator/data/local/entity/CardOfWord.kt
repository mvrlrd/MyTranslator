package ru.mvrlrd.mytranslator.data.local.entity


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "searching_history")
data class CardOfWord (
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo val text: String?,
    @ColumnInfo var translation: String?,
    @ColumnInfo val image_url: String?,
    @ColumnInfo val transcription: String?,
    @ColumnInfo val partOfSpeech: String?,
    @ColumnInfo val prefix: String?,
    @ColumnInfo var progress : Int
):Parcelable{
//    @Ignore
//    constructor(text:String?,translation: String? ): this(0,text,translation)

    override fun equals(other: Any?): Boolean {
        return if (other !is CardOfWord){
            false
        }else this.text == other.text
    }

    fun toStringArray(): Array<String?> {
        return arrayOf(
            this.id.toString(),
            this.text,
            this.translation,
            this.image_url,
            this.transcription,
            this.partOfSpeech,
            this.prefix)
    }
}
