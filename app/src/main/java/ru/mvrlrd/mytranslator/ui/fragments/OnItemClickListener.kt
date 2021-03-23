package ru.mvrlrd.mytranslator.ui.fragments



interface OnItemClickListener {
    fun onItemClick(categoryId: Long)
    fun onItemSwiped(categoryId: Long)
    fun onItemLongPressed(categoryId: Long)
}