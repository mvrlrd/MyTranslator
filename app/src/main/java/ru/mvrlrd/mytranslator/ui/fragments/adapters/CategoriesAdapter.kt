package ru.mvrlrd.mytranslator.ui.fragments.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.*
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import kotlinx.android.synthetic.main.item_category.view.*
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.CategoryState
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

    fun clearSelection(){
        for (i in collection){
            Log.e(TAG,"${i.isChecked}  ${i.name}     BEFORE")
        }
            tracker?.clearSelection()
        notifyDataSetChanged()
        for (i in collection){
            Log.e(TAG,"${i.isChecked}  ${i.name}     AFTER")
        }
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val cat = collection[position]
        tracker?.let{
//            if (cat.isChecked){
////                it.select(cat.categoryId)
////                cat.isChecked = false
//                holder.bind(cat, true)
//            }else {

                holder.bind(cat, it)
            }

        }

    fun catchUp(){
        for (item in collection){
            if (item.isChecked){
                item.isChecked = false
                tracker?.select(item.categoryId)
            }
        }
    }


    class CategoryHolder(private val listener: CategoriesAdapterListener, itemView: View) :
        RecyclerView.ViewHolder(itemView)
                , ItemTouchHelperViewHolder {

        @SuppressLint("SetTextI18n")
        fun bind(category: Category, _tracker: SelectionTracker<Long> ) {

//            Log.e(TAG,"$${category.name}  ${category.state}     ____________________")
            if  ((category.state==CategoryState.ACTIVATED_FROM_THE_BEGINNING)) {
                itemView.isActivated = true
                category.changeState()
                Log.e(TAG,"$${category.name}  ${category.state}")
            } else if(category.state == CategoryState.SELECTED_AFTER_ACTIVATING){
                    _tracker.select(category.categoryId)
                    category.changeState()
                Log.e(TAG,"$${category.name}  ${category.state}")
                itemView.isActivated = _tracker.isSelected(category.categoryId)

            }
            else if(_tracker.isSelected(category.categoryId)){
                category.changeState()
                itemView.isActivated = _tracker.isSelected(category.categoryId)
                Log.e(TAG,"$${category.name}  ${category.state}")
            }
            else if(!_tracker.isSelected(category.categoryId)&&(category.state == CategoryState.CHANGED)){
                category.changeState()
                itemView.isActivated = _tracker.isSelected(category.categoryId)
                Log.e(TAG,"$${category.name}  ${category.state}")
            }
//            Log.e(TAG,"$${category.name}  ${category.state}     ____________________")


//            else{
//                if (category.state == CategoryState.SELECTED_AFTER_ACTIVATING){
//                    category.changeState()
//                    _tracker.select(category.categoryId)
//                } else if ((_tracker.isSelected(category.categoryId)!=category.isChecked)){
//                    category.changeState()
//                    Log.e(TAG,"$${category.name} changed state to ${category.state}")
//                }else {
//                    category.changeState()
//                    Log.e(TAG,"$${category.name} state is ${category.state}")
//                }
//                itemView.isActivated = _tracker.isSelected(category.categoryId)
//            }


//            when(_isSelected){
//                true->Log.e(TAG,"$${category.name} id# ${category.categoryId} selected")
//                false->Log.e(TAG,"$${category.name} id# ${category.categoryId} UNselected")
//            }
















            itemView.textViewItem.text = category.name
            itemView.category_icon_image_view.load(category.icon.toInt())
//            itemView.isSelected = category.isChecked
            itemView.percentageTextView.text = "${category.averageProgress.roundToInt()}%"
//            itemView.edit_icon_image_view.setOnClickListener {
//                listener.editCurrentItem(category)
//            }
            itemView.setOnClickListener {
//                checkUncheckItem(itemView,category)
                listener.onItemClick(category.categoryId, category.isChecked)
            }
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
