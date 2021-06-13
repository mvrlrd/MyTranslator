package ru.mvrlrd.mytranslator.ui.fragments.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_word.view.*
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.ui.fragments.OnItemClickListener
import ru.mvrlrd.mytranslator.ui.old.old.ItemTouchHelperAdapter
import java.util.*
import kotlin.properties.Delegates

private const val TAG = "WordsAdapter"

class WordsAdapter(private val onSwipeListener: OnItemClickListener) :
    RecyclerView.Adapter<WordsAdapter.WordHolder>(), ItemTouchHelperAdapter {
    internal var collection: MutableList<Card> by
    Delegates.observable(mutableListOf()) { _, _, _ -> notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_word, parent, false)
        return WordHolder(
            view
        )
//        TranslationHolder(parent.inflate(R.layout.recycler_item))
    }

    override fun onBindViewHolder(holder: WordHolder, position: Int) {
        holder.bind(collection[position])
        holder.itemView.setOnClickListener {
            onSwipeListener.onItemClick(collection[position].id)
        }
        holder.itemView.setOnLongClickListener {
            onSwipeListener.onItemLongPressed(collection[position].id)
            true
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
        onSwipeListener.onItemSwiped(collection[position].id)
        Log.e(TAG, "${collection[position].word}    onItemDismissed()")
        collection.removeAt(position)
        notifyItemRemoved(position)
    }

    class WordHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        //        , ItemTouchHelperViewHolder {

        @SuppressLint("SetTextI18n")
        fun bind(card: Card) {
            itemView.wordTextView.text = card.word
            itemView.translationTextView.text = card.translation
            itemView.transcriptionTextView2.let {
                it.text = hideEmptyView(it, "[${card.transcription}]")
            }
            itemView.antonymTextView.text = ""
        }

        private fun hideEmptyView(view: View, str: String?): String? {
            if (str != "[_]") {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
            return str
        }
    }
}