package ru.mvrlrd.mytranslator.ui.fragments.categories.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import kotlinx.android.synthetic.main.item_category.view.*
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.ui.fragments.OnItemClickListener
import kotlin.properties.Delegates

class CategoriesAdapter(
    private val onOnItemClickListener: OnItemClickListener
    ) : RecyclerView.Adapter<CategoriesAdapter.CategoryHolder>() {

    internal var collection: MutableList<Category> by
    Delegates.observable(mutableListOf()) { _, _, _ -> notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bind(collection[position])
        holder.itemView.setOnClickListener {
            onOnItemClickListener.onItemClick(collection[position].categoryId)
        }
    }

    override fun getItemCount() = collection.size


    class CategoryHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        //        , ItemTouchHelperViewHolder {

        fun bind(category: Category) {
            itemView.textViewItem.text = category.name
            itemView.categoryIcon.load(category.icon.toInt())
        }
    }
}
