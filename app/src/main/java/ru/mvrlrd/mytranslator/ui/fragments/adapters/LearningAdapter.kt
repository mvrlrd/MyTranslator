package ru.mvrlrd.mytranslator.ui.fragments.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import kotlinx.android.synthetic.main.item_category.view.*
import kotlinx.android.synthetic.main.item_learning_card.view.*
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.ui.old.old.ItemTouchHelperAdapter
import ru.mvrlrd.mytranslator.ui.old.old.ItemTouchHelperViewHolder
import java.util.*
import kotlin.properties.Delegates

private val TAG = "LearningAdapter"
class LearningAdapter(
private val onSwipeListener: LearningAdapterListener
) : RecyclerView.Adapter<LearningAdapter.LearningCardsHolder>(), ItemTouchHelperAdapter {

    internal var collection: MutableList<CardOfWord> by
    Delegates.observable(mutableListOf()) { _, _, _ -> notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LearningCardsHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_learning_card, parent, false)
        return LearningCardsHolder(
            onSwipeListener,  view
        )
    }

    override fun onBindViewHolder(holder: LearningCardsHolder, position: Int) {

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
        onSwipeListener.onItemSwiped(collection[position].id)
        Log.e(TAG, "${collection[position].text}    onItemDismissed()")
        collection.removeAt(position)
        notifyItemRemoved(position)
    }

    class LearningCardsHolder(private val listener: LearningAdapterListener, itemView: View) :
        RecyclerView.ViewHolder(itemView)
        , ItemTouchHelperViewHolder {

        fun bind(cardOfWord: CardOfWord) {

            itemView.learningImageView.load("https:${cardOfWord.image_url}")
            itemView.learningTextView.text = cardOfWord.text
            itemView.learningTextView2.visibility = View.GONE
            itemView.learningTextView2.text = cardOfWord.translation
            itemView.setOnClickListener {
                if (itemView.learningTextView2.visibility == View.VISIBLE){
                    itemView.learningTextView2.visibility = View.GONE
                }else{
                    itemView.learningTextView2.visibility = View.VISIBLE
                }

                listener.onItemClick(cardOfWord.id)
            }

        }

        override fun onItemSelected() {

        }

        override fun onItemClear() {

        }
    }

    interface LearningAdapterListener {
        fun onItemClick(id: Long)
        fun onItemSwiped(categoryId: Long)
        fun onItemLongPressed(v: View, category: Category)
//        fun onItemSwiped(recipe : RecipeInformation)
//        fun onFavouriteIconClick(recipeData: RecipeInformation)
    }

}