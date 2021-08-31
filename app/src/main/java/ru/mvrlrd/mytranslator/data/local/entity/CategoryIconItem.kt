package ru.mvrlrd.mytranslator.data.local.entity

class CategoryIconItem(val drawableId: Int) {
    override fun equals(other: Any?): Boolean {
         if(other is CategoryIconItem){
             return (drawableId==other.drawableId)
         }
        return false
    }
}