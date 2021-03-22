package ru.mvrlrd.mytranslator.ui.old.old.recycler_tags

import ru.mvrlrd.mytranslator.data.local.entity.Category

interface OnItemChecked {
    var _checkedList:MutableList<Category>
    fun fillCheckedList()

}