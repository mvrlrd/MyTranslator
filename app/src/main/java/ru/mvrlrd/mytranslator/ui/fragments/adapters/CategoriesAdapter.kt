package ru.mvrlrd.mytranslator.ui.fragments.adapters

import android.util.Log
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import kotlinx.android.synthetic.main.item_category.view.*
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.ui.old.old.ItemTouchHelperAdapter
import ru.mvrlrd.mytranslator.ui.old.old.ItemTouchHelperViewHolder
import java.util.*
import kotlin.properties.Delegates

private const val TAG = "CategoriesAdapter"

class CategoriesAdapter(
    private val onSwipeListener: CategoriesAdapterListener
    ) : RecyclerView.Adapter<CategoriesAdapter.CategoryHolder>(), ItemTouchHelperAdapter {

    internal var collection: MutableList<Category> by
    Delegates.observable(mutableListOf()) { _, _, _ -> notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryHolder(
           onSwipeListener,  view
        )
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bind(collection[position])
    }

    override fun getItemCount() = collection.size

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Log.e("onItemMove", "run ")
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(collection, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(collection, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
        onSwipeListener.onItemSwiped(collection[position].categoryId)
        Log.e(TAG, "${collection[position].name}    onItemDismissed()")
        collection.removeAt(position)
        notifyItemRemoved(position)
    }

    class CategoryHolder(private val listener: CategoriesAdapterListener, itemView: View) :
        RecyclerView.ViewHolder(itemView)
                , ItemTouchHelperViewHolder {

        fun bind(category: Category) {
            itemView.textViewItem.text = category.name
            itemView.categoryIcon.load(category.icon.toInt())
            itemView.categoryIcon.isSelected = category.isChecked

            itemView.setOnClickListener {
                itemView.categoryIcon.isSelected = !itemView.categoryIcon.isSelected
                category.isChecked = !category.isChecked

                listener.onItemClick(itemView.categoryIcon, category)

            }

            itemView.setOnLongClickListener {

                listener.onItemLongPressed(category.categoryId)
                true
            }

            val categoryItemTransitionName =
                itemView.resources.getString(R.string.word_list_transition_name, category.categoryId)
            itemView.transitionName = categoryItemTransitionName
        }

        override fun onItemSelected() {

        }

        override fun onItemClear() {

        }
    }

//    interface ListItem{
//
//        fun getTypeOfItem()
//
//    }

    interface CategoriesAdapterListener {
        fun onItemClick(v:View,category: Category)
        fun onItemSwiped(categoryId: Long)
        fun onItemLongPressed(id: Long)
//        fun onItemSwiped(recipe : RecipeInformation)
//        fun onFavouriteIconClick(recipeData: RecipeInformation)
    }

}
