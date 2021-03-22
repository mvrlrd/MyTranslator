package ru.mvrlrd.mytranslator.ui.fragments.categories.add_words_to_category.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_word.view.*
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import kotlin.properties.Delegates

class WordsAdapter:
    RecyclerView.Adapter<WordsAdapter.WordHolder>()
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
//            onOnItemClickListener.onItemClick(collection[position].categoryId)
        }
    }



    override fun getItemCount() = collection.size






    class WordHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        //        , ItemTouchHelperViewHolder {

        fun bind(cardOfWord : CardOfWord) {
            itemView.wordTextView.text = cardOfWord.text

        }

    }





}