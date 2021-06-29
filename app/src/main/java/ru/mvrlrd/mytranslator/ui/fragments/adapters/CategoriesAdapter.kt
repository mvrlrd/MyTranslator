package ru.mvrlrd.mytranslator.ui.fragments.adapters

import android.util.Log
import android.view.*
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import kotlinx.android.synthetic.main.item_category.view.*
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.ui.old.old.ItemTouchHelperAdapter
import ru.mvrlrd.mytranslator.ui.old.old.ItemTouchHelperViewHolder
import java.util.*
import kotlin.math.roundToInt
import kotlin.properties.Delegates

private const val TAG = "CategoriesAdapter"

class CategoriesAdapter(
    private val onSwipeListener: CategoriesAdapterListener
    ) : RecyclerView.Adapter<CategoriesAdapter.CategoryHolder>(), ItemTouchHelperAdapter {

    internal var collection: MutableList<Category> by
    Delegates.observable(mutableListOf()) { _, _, _ -> notifyDataSetChanged() }
    init {
        setHasStableIds(true)
    }
    var tracker: SelectionTracker<Long>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryHolder(
           onSwipeListener,  view
        )
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val cat = collection[position]
        tracker?.let{
            holder.bind(cat, it.isSelected(cat.categoryId))
        }

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

    fun updateCollection(updatedCollection: List<Category>){
        collection = updatedCollection as MutableList<Category>
        notifyDataSetChanged()
    }


    fun getAll() = collection
    fun getSelected() = run { collection.filter { it.isChecked } }
    override fun getItemId(position: Int): Long = collection[position].categoryId

    class CategoryHolder(private val listener: CategoriesAdapterListener, itemView: View) :
        RecyclerView.ViewHolder(itemView)
                , ItemTouchHelperViewHolder {

        fun bind(category: Category, selected: Boolean = false) {
            itemView.isActivated = selected

            itemView.textViewItem.text = category.name
            itemView.category_icon_image_view.load(category.icon.toInt())
//            itemView.isSelected = category.isChecked
            itemView.percentageTextView.text = "${category.averageProgress.roundToInt()}%"
//            itemView.edit_icon_image_view.setOnClickListener {
//                listener.editCurrentItem(category)
//            }
//            itemView.setOnClickListener {
//                checkUncheckItem(itemView,category)
//            }
//            itemView.setOnLongClickListener {
//                listener.onItemLongPressed(category.categoryId)
//                true
//            }
            val categoryItemTransitionName =
                itemView.resources.getString(R.string.word_list_transition_name, category.categoryId)
            itemView.transitionName = categoryItemTransitionName
        }

        private fun checkUncheckItem(v: View, category: Category){
            category.isChecked = !category.isChecked
            v.isSelected = category.isChecked
            listener.onItemClick(category.categoryId, category.isChecked)
        }

        override fun onItemSelected() {

        }

        override fun onItemClear() {

        }


        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? = itemId
            }

    }


    interface CategoriesAdapterListener {
        fun onItemClick(id: Long, isChecked: Boolean)
        fun onItemSwiped(categoryId: Long)
        fun onItemLongPressed(id: Long)
        fun editCurrentItem(category: Category)
    }
}
