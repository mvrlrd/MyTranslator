package ru.mvrlrd.mytranslator.ui.fragments.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_translations.view.*
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.ui.old.old.ItemTouchHelperAdapter
import java.util.*
import kotlin.properties.Delegates

private val TAG ="TranslationAdapter"
class TranslationAdapter(private val onSwipeListener: OnClickTranslationListener):
    RecyclerView.Adapter<TranslationAdapter.TranslationHolder>(), ItemTouchHelperAdapter
{
    internal var collection: MutableList<String> by
    Delegates.observable(mutableListOf()) { _, _, _ -> notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranslationHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_translations, parent, false)
        return TranslationHolder(
            view
        )
//        TranslationHolder(parent.inflate(R.layout.recycler_item))
    }


    override fun onBindViewHolder(holder: TranslationHolder, position: Int) {
        holder.bind(collection[position])
        holder.itemView.setOnClickListener {
            onSwipeListener.onClickItem(collection[holder.adapterPosition])
            collection.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
        }
//
//        holder.itemView.setOnLongClickListener {
//            onSwipeListener.onItemLongPressed(collection[position].id)
//            true
//        }
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
//        onSwipeListener.onItemSwiped(position.toLong())
//        Log.e(TAG, "${collection[position]}    onItemDismissed()")
//        collection.removeAt(position)
//        notifyItemRemoved(position)
    }

    class TranslationHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        //        , ItemTouchHelperViewHolder {

        @SuppressLint("SetTextI18n")
        fun bind(translation : String) {
            itemView.translation_item_TextView.text = translation


        }

    }

    interface OnClickTranslationListener{
        fun onClickItem(translation: String)
    }
}