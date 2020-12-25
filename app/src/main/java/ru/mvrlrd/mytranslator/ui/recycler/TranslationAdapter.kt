package ru.mvrlrd.mytranslator.ui.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import kotlinx.android.synthetic.main.recycler_item.view.*
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler
//import ru.mvrlrd.mytranslator.service.inflate
import kotlin.properties.Delegates

class TranslationAdapter :
    RecyclerView.Adapter<TranslationAdapter.TranslationHolder>() {

    internal var collection: List<MeaningModelForRecycler> by
    Delegates.observable(emptyList()) { _, _, _ -> notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):TranslationHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item, parent, false)
        return TranslationHolder(view)
//        TranslationHolder(parent.inflate(R.layout.recycler_item))
    }


    override fun onBindViewHolder(holder: TranslationHolder, position: Int) =
        holder.bind(collection[position])

    override fun getItemCount() = collection.size

    class TranslationHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(translationView: MeaningModelForRecycler) {
            itemView.image_translation.load("https:${translationView.image_url}")
            itemView.recycler_text.text = translationView.text
            itemView.recycler_translation.text = translationView.translation

        }
    }

}