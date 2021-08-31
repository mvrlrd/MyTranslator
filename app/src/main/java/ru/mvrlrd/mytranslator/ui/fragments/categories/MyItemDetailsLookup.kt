package ru.mvrlrd.mytranslator.ui.fragments.categories

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import ru.mvrlrd.mytranslator.ui.fragments.adapters.CategoriesAdapter

class MyItemDetailsLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Long>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as CategoriesAdapter.CategoryHolder)
                .getItemDetails()
        }
        return null
    }
}