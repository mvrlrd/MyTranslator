package ru.mvrlrd.mytranslator.ui.old.old

import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler

interface OnSwipeListener {
    fun onItemSwiped(meaningModelForRecycler: MeaningModelForRecycler)
    fun onItemLongPressed(currentCardId: Long)
}