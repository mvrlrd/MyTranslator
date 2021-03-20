package ru.mvrlrd.mytranslator.ui.fragments.categories.add_category_dialog.recycler

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import kotlinx.android.synthetic.main.category_item.view.*
import kotlinx.android.synthetic.main.icon_item.view.*
import ru.mvrlrd.mytranslator.R

private val TAG = "IconsAdapter"

class IconsAdapter(private val listener: IconAdapterListener) :
    RecyclerView.Adapter<IconsAdapter.IconHolder>()
{
     var collection = listOf(
         R.drawable.bank,
         R.drawable.furniture,
         R.drawable.books,
         R.drawable.car,
         R.drawable.city,
         R.drawable.forest,
         R.drawable.gas,
         R.drawable.human,
         R.drawable.key,
         R.drawable.mirror,
         R.drawable.paint,
         R.drawable.renovation,
         R.drawable.sale
     )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.icon_item, parent, false)
        return IconHolder(
            view, listener
        )
//        TranslationHolder(parent.inflate(R.layout.recycler_item))
    }


    override fun onBindViewHolder(holder: IconHolder, position: Int) {
         holder.bind(collection[position])
    }

    override fun getItemCount() = collection.size






    class IconHolder(itemView: View, private val listener: IconAdapterListener) :
        RecyclerView.ViewHolder(itemView) {
        //        , ItemTouchHelperViewHolder {

        fun bind(idOfDrawable: Int) {
            itemView.iconImageView.load(idOfDrawable)
            itemView.iconImageView.setOnClickListener {
                Log.e(TAG, " $idOfDrawable")
                    listener.onIconClicked(idOfDrawable)
                itemView.iconImageView.isSelected = !itemView.iconImageView.isSelected
            }


        }
    }

        interface IconAdapterListener {
            fun onIconClicked(id: Int)
        }


    }