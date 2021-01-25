package ru.mvrlrd.mytranslator.ui.recycler

import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler

interface OnSwipeListener {
    fun onItemSwiped(meaningModelForRecycler: MeaningModelForRecycler)
    fun onItemLongPressed()
}