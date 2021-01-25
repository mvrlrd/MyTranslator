package ru.mvrlrd.mytranslator.data.local.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "group_tags")
data class GroupTag(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("tagId")
    val tagId: Long,
    @SerializedName("tag")
    val tag: String,

    var isChecked: Boolean




) {
    override fun equals(other: Any?): Boolean {
        return if (other !is GroupTag){
            false
        }else this.tag == other.tag
    }
}