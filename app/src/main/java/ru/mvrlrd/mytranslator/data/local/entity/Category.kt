package ru.mvrlrd.mytranslator.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "group_tags")
data class Category(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val categoryId: Long,
    @SerializedName("name")
    val name: String,
    val icon: String,
    var isChecked: Boolean
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        return if (other !is Category) {
            false
        } else this.name == other.name
    }

    override fun toString(): String {
        return "$name $icon"


    }
//    override fun toString(): String {
//        return "{\"categoryId\":\"$categoryId\"," +
//                "\"name\":\"$name\"," +
//                "\"icon\":\"$icon\","+
//                "\"isChecked\":\"$isChecked\"}"
//    }
}


