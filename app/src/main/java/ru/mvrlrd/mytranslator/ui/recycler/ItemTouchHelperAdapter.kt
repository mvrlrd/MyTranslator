package ru.mvrlrd.mytranslator.ui.recycler

import android.os.Vibrator

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition : Int, toPosition : Int): Boolean
    fun onItemDismiss(position : Int)
}