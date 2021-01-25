package ru.mvrlrd.mytranslator.ui.recycler_tags

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_item.view.*
import kotlinx.android.synthetic.main.recycler_tag_item.view.*
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.GroupTag
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler
import ru.mvrlrd.mytranslator.ui.recycler.ItemTouchHelperAdapter
import ru.mvrlrd.mytranslator.ui.recycler.OnSwipeListener
import java.util.*
import kotlin.properties.Delegates

class TagsAdapter :
        RecyclerView.Adapter<TagsAdapter.TagsHolder>(){

    val collection = listOf(GroupTag(1,"series/movies"),
        GroupTag(2,"programming"),
        GroupTag(3,"travelling"),
        GroupTag(4,"games"),
        GroupTag(5,"sport"))

//        private var collection: MutableList<GroupTag> by
//        Delegates.observable(mutableListOf()) { _, _, _ -> notifyDataSetChanged() }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_tag_item, parent, false)
            return TagsHolder(
                view
            )
//        TranslationHolder(parent.inflate(R.layout.recycler_item))
        }


        override fun onBindViewHolder(holder: TagsHolder, position: Int) =
            holder.bind(collection[position])

        override fun getItemCount() = collection.size





        class TagsHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            //        , ItemTouchHelperViewHolder {



            fun bind(tag: GroupTag) {
                itemView.tagCheckbox.text = tag.tag
            }

//        override fun onItemSelected() {
//            itemView.setBackgroundColor(Color.BLACK)
//        }

//        override fun onItemClear() {
//            itemView.setBackgroundColor(0)
//        }
        }





    }


