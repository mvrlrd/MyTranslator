package ru.mvrlrd.mytranslator.data.local.entity.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord

data class CardWithTag(
    @Embedded val card: CardOfWord,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId",
        associateBy = Junction(CardTagCrossRef::class)
    )
    val tags : List<Category>
)