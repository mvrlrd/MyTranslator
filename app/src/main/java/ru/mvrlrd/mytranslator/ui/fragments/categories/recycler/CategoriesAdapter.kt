package ru.mvrlrd.mytranslator.ui.fragments.categories.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import kotlinx.android.synthetic.main.category_item.view.*
import kotlinx.android.synthetic.main.icon_item.view.*
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.Category
import kotlin.properties.Delegates

class CategoriesAdapter :
    RecyclerView.Adapter<CategoriesAdapter.CategoryHolder>()
     {
         internal var collection: MutableList<Category> by
         Delegates.observable(mutableListOf()) { _, _, _ -> notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item, parent, false)
        return CategoryHolder(
            view
        )
//        TranslationHolder(parent.inflate(R.layout.recycler_item))
    }


    override fun onBindViewHolder(holder: CategoryHolder, position: Int) =
        holder.bind(collection[position])

    override fun getItemCount() = collection.size






    class CategoryHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        //        , ItemTouchHelperViewHolder {

        fun bind(category : Category) {
            itemView.textViewItem.text = category.name
            itemView.categoryIcon.load(category.icon.toInt())

        }

    }





}
