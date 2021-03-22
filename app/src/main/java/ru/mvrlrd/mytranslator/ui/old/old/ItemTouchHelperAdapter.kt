package ru.mvrlrd.mytranslator.ui.old.old

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition : Int, toPosition : Int): Boolean
    fun onItemDismiss(position : Int)
}