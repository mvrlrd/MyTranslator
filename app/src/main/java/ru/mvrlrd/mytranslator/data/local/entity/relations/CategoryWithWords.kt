package ru.mvrlrd.mytranslator.data.local.entity.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.data.local.entity.Category

class CategoryWithWords(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id",
        associateBy = Junction(CardTagCrossRef::class)
    )
    val cards : List<CardOfWord>
)