package ru.mvrlrd.mytranslator.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "group_tags")
data class Category(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val categoryId: Long,
    @SerializedName("name")
    val name: String,
    val icon: String

) {
    override fun equals(other: Any?): Boolean {
        return if (other !is Category){
            false
        }else this.name == other.name
    }

}