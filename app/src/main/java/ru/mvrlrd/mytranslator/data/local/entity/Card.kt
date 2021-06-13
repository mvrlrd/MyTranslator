package ru.mvrlrd.mytranslator.data.local.entity


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "cards_db")
data class Card(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo val word: String?,
    @ColumnInfo var translation: String?,
    @ColumnInfo val image_url: String?,
    @ColumnInfo val transcription: String?,
    @ColumnInfo val partOfSpeech: String?,
    @ColumnInfo val prefix: String?,
    @ColumnInfo var progress: Int
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        return if (other !is Card) {
            false
        } else this.word == other.word
    }

    fun toStringArray(): Array<String?> {
        return arrayOf(
            this.id.toString(),
            this.word,
            this.translation,
            this.image_url,
            this.transcription,
            this.partOfSpeech,
            this.prefix
        )
    }
}
