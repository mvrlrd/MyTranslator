package ru.mvrlrd.mytranslator.data.local.entity.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.mvrlrd.mytranslator.data.local.entity.GroupTag
import ru.mvrlrd.mytranslator.data.local.entity.HistoryEntity

data class CardWithTag(
    @Embedded val card: HistoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "tagId",
        associateBy = Junction(CardTagCrossRef::class)
    )
    val tags : List<GroupTag>
)