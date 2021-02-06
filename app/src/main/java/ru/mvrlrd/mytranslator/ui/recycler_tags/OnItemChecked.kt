package ru.mvrlrd.mytranslator.ui.recycler_tags

import ru.mvrlrd.mytranslator.data.local.entity.GroupTag

interface OnItemChecked {
    var checkedList:MutableList<GroupTag>
    fun fillCheckedList()
}