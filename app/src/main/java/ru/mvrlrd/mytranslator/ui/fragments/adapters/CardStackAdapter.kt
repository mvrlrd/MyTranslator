package ru.mvrlrd.mytranslator.ui.fragments.adapters

import android.util.Log
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
import kotlin.random.Random

private val TAG ="CardStackAdapter"
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
if (collection.isNotEmpty()) {
    var circ = position % collection.size
    var rand = shuffleCards(collection.size-1)

    Log.e(TAG, "pos = $position ${collection[rand].text}   rand =$rand   circ=$circ     adPos = ${holder.adapterPosition}")
    holder.bind(collection[rand])
}else{

}
    }
    fun shuffleCards(quantityOfCards: Int): Int{
        return (0..quantityOfCards).random()
    }

    override fun getItemCount() = Int.MAX_VALUE


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(cardOfWord: CardOfWord) {
            if (cardOfWord.image_url !="_"){
                itemView.main_image_view.load("https:${cardOfWord.image_url}")
            }
            itemView.item_card_word.text = cardOfWord.text
            itemView.item_card_transcription.let {
                it.text = hideEmptyView(it, "[${cardOfWord.transcription}]")
                itemView.item_card_translation.text = cardOfWord.translation
            }
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