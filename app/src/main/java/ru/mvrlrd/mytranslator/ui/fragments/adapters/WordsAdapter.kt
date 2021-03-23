package ru.mvrlrd.mytranslator.ui.fragments.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_word.view.*
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.ui.fragments.OnItemClickListener
import ru.mvrlrd.mytranslator.ui.old.old.ItemTouchHelperAdapter
import java.util.*
import kotlin.properties.Delegates

private val TAG = "WordsAdapter"
class WordsAdapter(private val onSwipeListener: OnItemClickListener):
    RecyclerView.Adapter<WordsAdapter.WordHolder>(), ItemTouchHelperAdapter
{
    internal var collection: MutableList<CardOfWord> by
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
        Log.e(TAG, "${collection[position].text}    onItemDismissed()")
        collection.removeAt(position)
        notifyItemRemoved(position)
    }

    class WordHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        //        , ItemTouchHelperViewHolder {

        @SuppressLint("SetTextI18n")
        fun bind(cardOfWord : CardOfWord) {
            itemView.wordTextView.text = cardOfWord.text
            itemView.translationTextView.text = cardOfWord.translation
            itemView.transcriptionTextView2.text = cardOfWord.transcription
            itemView.antonymTextView.text = "antonym not ready yet"


        }

    }
}