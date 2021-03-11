package ru.mvrlrd.mytranslator.data.local.entity.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.mvrlrd.mytranslator.data.local.entity.GroupTag
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord

data class CardWithTag(
    @Embedded val card: CardOfWord,
    @Relation(
        parentColumn = "id",
        entityColumn = "tagId",
        associateBy = Junction(CardTagCrossRef::class)
    )
    val tags : List<GroupTag>
)