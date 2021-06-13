package ru.mvrlrd.mytranslator.data.local.entity.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.Card

data class CardWithCategory(
    @Embedded val card: Card,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId",
        associateBy = Junction(CardCategoryCrossRef::class)
    )
    val categories : List<Category>
)