package ru.mvrlrd.mytranslator.data.local.entity.relations

import androidx.room.Entity

@Entity(primaryKeys = ["id", "categoryId"])
data class CardCategoryCrossRef(
    val id: Long,
    val categoryId: Long
)


