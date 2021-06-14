package ru.mvrlrd.mytranslator.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "categories_db")
data class Category(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val categoryId: Long = 0,
    @SerializedName("name")
    val name: String,
    val icon: String,
    var isChecked: Boolean = false,
    var averageProgress: Double = 0.0
) : Parcelable {
    override fun equals(other: Any?): Boolean {
         if (other !is Category) {
           return false
        } else if((this.name == other.name)&&(this.icon==other.icon)){
            return true
         }
       return false
    }

    override fun toString(): String {
        return "$categoryId,$name,$icon,$isChecked"
    }
}


