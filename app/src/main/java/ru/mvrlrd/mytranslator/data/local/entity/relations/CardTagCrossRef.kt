package ru.mvrlrd.mytranslator.data.local.entity.relations

import androidx.room.Entity

@Entity(primaryKeys = ["id", "tagId"])
data class CardTagCrossRef(
    val id: Long,
    val tagId: Long
)


