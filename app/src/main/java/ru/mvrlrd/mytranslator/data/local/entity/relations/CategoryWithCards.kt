package ru.mvrlrd.mytranslator.data.local.entity.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.data.local.entity.Category

class CategoryWithCards(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id",
        associateBy = Junction(CardCategoryCrossRef::class)
    )
    val cards: List<Card>
) {
    fun averageProgress() = cards.map { it -> it.progress }.average()
}