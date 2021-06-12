package ru.mvrlrd.mytranslator.ui.fragments.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorLong
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import kotlinx.android.synthetic.main.item_card.view.*
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.ui.fragments.learning.LearningProcess
import kotlin.properties.Delegates

private val TAG ="CardStackAdapter"
class CardStackAdapter(private val learningProcessHandler: LearningProcess) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>(), CardStackListener {

    internal var collection: MutableList<CardOfWord> by
    Delegates.observable(mutableListOf()) { _, _, _ -> notifyDataSetChanged() }

    var currentCardPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return ViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (collection.isNotEmpty()) {
            var circ = 0
            collection.size.let {
                circ = position % it
            }
//    Log.e(TAG, "${collection[circ].text}   circ= $circ   pos= $position")
            holder.bind(collection[circ])
        } else {

        }
    }
    private fun shuffleCards(quantityOfCards: Int): Int{
        return (0..quantityOfCards).random()
    }

    override fun getItemCount() = Int.MAX_VALUE


    override fun onCardAppeared(view: View?, position: Int) {
        if (collection.isNotEmpty()) {
            var circ = 0
            collection.size.let {
                circ = position % it
                currentCardPosition = circ
            }
            Log.e(TAG, "onCardAppeared      ${collection[circ].text}  circ=$circ   current=$currentCardPosition   pos=$position")
        }
    }
    override fun onCardDragging(direction: Direction?, ratio: Float) {
//        Log.e(TAG, "onCardDragging")
    }

    override fun onCardRewound() {
//        Log.e(TAG, "onCardRewound")
    }

    override fun onCardCanceled() {
//        Log.e(TAG, "onCardCanceled")
    }

    override fun onCardDisappeared(view: View?, position: Int) {
//        Log.e(TAG, "onCardDisappeared")
    }

    override fun onCardSwiped(direction: Direction?) {
        when (direction) {
            Direction.Left -> {
                collection[currentCardPosition].progress += 25
                if (collection[currentCardPosition].progress == 100) {
                    removeLearnedItem()
                }
            }
            Direction.Right -> {
                collection[currentCardPosition].progress = 0
            }
        }
    }
    private fun removeLearnedItem(){
        collection.removeAt(currentCardPosition)
        if (collection.isEmpty()){
            learningProcessHandler.finishLearningProcess()
        }
        if (collection.isNotEmpty()){
            notifyDataSetChanged()
        }

    }

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