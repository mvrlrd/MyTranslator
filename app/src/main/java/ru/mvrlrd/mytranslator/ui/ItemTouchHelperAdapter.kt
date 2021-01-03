package ru.mvrlrd.mytranslator.ui

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition : Int, toPosition : Int): Boolean
    fun onItemDismiss(position : Int)
}