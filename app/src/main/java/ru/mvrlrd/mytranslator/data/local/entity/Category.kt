package ru.mvrlrd.mytranslator.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
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

    @Ignore
    var state: CategoryState = if (isChecked) CategoryState.ACTIVATED_FROM_THE_BEGINNING else CategoryState.STABLE

    fun changeState() {
        state = when (state) {
            CategoryState.ACTIVATED_FROM_THE_BEGINNING -> {
                CategoryState.SELECTED_AFTER_ACTIVATING
            }
            CategoryState.SELECTED_AFTER_ACTIVATING -> {
                CategoryState.CHANGED
            }
            CategoryState.STABLE -> {
                CategoryState.CHANGED
            }
            else -> {CategoryState.STABLE}
        }
    }


    override fun equals(other: Any?): Boolean {
         if (other !is Category) {
           return false
        } else if((this.name == other.name)&&(this.icon==other.icon)){
            return true
         }
       return false
    }

    override fun toString(): String {
        return "$categoryId,$name,$icon"
    }
}


