package ru.mvrlrd.mytranslator.ui.fragments.adapters

import android.util.Log
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import kotlinx.android.synthetic.main.item_category.view.*
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.ui.fragments.OnItemClickListener
import ru.mvrlrd.mytranslator.ui.old.old.ItemTouchHelperAdapter
import ru.mvrlrd.mytranslator.ui.old.old.ItemTouchHelperViewHolder
import java.util.*
import kotlin.properties.Delegates

private val TAG = "CategoriesAdapter"
class CategoriesAdapter(
    private val onSwipeListener: RecipesAdapterListener
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

    class CategoryHolder(private val listener: RecipesAdapterListener, itemView: View) :
        RecyclerView.ViewHolder(itemView)
                , ItemTouchHelperViewHolder {

        fun bind(category: Category) {
            itemView.textViewItem.text = category.name
            itemView.categoryIcon.load(category.icon.toInt())
            itemView.categoryIcon.isSelected = category.isChecked

            itemView.setOnLongClickListener {
                listener.onItemClick( category.categoryId)
                true
            }

            itemView.setOnClickListener {
                itemView.categoryIcon.isSelected = !itemView.categoryIcon.isSelected
                category.isChecked = !category.isChecked
                listener.onItemLongPressed(itemView.categoryIcon,category)

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

    interface RecipesAdapterListener {
        fun onItemClick(id: Long)
        fun onItemSwiped(categoryId: Long)
        fun onItemLongPressed(v:View,category: Category)
//        fun onItemSwiped(recipe : RecipeInformation)
//        fun onFavouriteIconClick(recipeData: RecipeInformation)
    }

}
