package ru.mvrlrd.mytranslator.ui.recycler

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_item.view.*
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler
import java.util.*
import kotlin.properties.Delegates

class TranslationAdapter(private val swipeListener : OnSwipeListener) :
    RecyclerView.Adapter<TranslationAdapter.TranslationHolder>(),
    ItemTouchHelperAdapter {

    internal var collection: MutableList<MeaningModelForRecycler> by
    Delegates.observable(mutableListOf()) { _, _, _ -> notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranslationHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item, parent, false)
        return TranslationHolder(
            view
        )
//        TranslationHolder(parent.inflate(R.layout.recycler_item))
    }


    override fun onBindViewHolder(holder: TranslationHolder, position: Int) =
        holder.bind(collection[position],swipeListener)

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemDismiss(position: Int) {
        Log.e("onItemDismiss", "run ")

        swipeListener.onItemSwiped(collection[position])

        println("${collection[position].translation}    swiped")
        collection.removeAt(position)
        notifyItemRemoved(position)

    }

    class TranslationHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        //        , ItemTouchHelperViewHolder {





        @SuppressLint("SetTextI18n")
        fun bind(meaningModelForRecycler: MeaningModelForRecycler, swipeListener: OnSwipeListener) {
            itemView.recycler_text.text = meaningModelForRecycler.text
            itemView.recycler_translation.text = meaningModelForRecycler.translation
//            itemView.image_translation.load("https:${meaningModelForRecycler.image_url}")
            itemView.partOfSpeechTextView.text = meaningModelForRecycler.partOfSpeech
            itemView.prefixTextView.text = meaningModelForRecycler.prefix ?: ""
            itemView.transcriptionTextView.text = "[${meaningModelForRecycler.transcription}]"
            

            // long press on recycler item
            itemView.setOnLongClickListener(View.OnLongClickListener {
                Log.e("TAG","fffff   ${meaningModelForRecycler.id}")
                swipeListener.onItemLongPressed(meaningModelForRecycler.id)
//                it.recycler_text.text = "aaaaa"
                false
            })
        }

//        override fun onItemSelected() {
//            itemView.setBackgroundColor(Color.BLACK)
//        }

//        override fun onItemClear() {
//            itemView.setBackgroundColor(0)
//        }
    }





}

