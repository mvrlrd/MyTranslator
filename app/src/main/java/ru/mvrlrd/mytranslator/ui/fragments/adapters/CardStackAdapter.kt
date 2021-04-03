package ru.mvrlrd.mytranslator.ui.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.yuyakaido.android.cardstackview.CardStackListener
import kotlinx.android.synthetic.main.item_card.view.*
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import kotlin.properties.Delegates

class CardStackAdapter : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    internal var collection: MutableList<CardOfWord> by
    Delegates.observable(mutableListOf()) { _, _, _ -> notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return ViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(collection[position])
    }

    override fun getItemCount() = collection.size


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(cardOfWord: CardOfWord) {
            itemView.main_image_view.load("https:${cardOfWord.image_url}")
            itemView.item_card_word.text = cardOfWord.text
            itemView.item_card_transcription.text = ("[${cardOfWord.transcription}]")
            itemView.item_card_translation.text = cardOfWord.translation
        }
    }
}