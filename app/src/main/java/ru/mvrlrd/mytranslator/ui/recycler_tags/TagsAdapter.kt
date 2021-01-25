package ru.mvrlrd.mytranslator.ui.recycler_tags

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_tag_item.view.*
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.GroupTag
import kotlin.properties.Delegates


class TagsAdapter :
        RecyclerView.Adapter<TagsAdapter.TagsHolder>(){

//    val collection = listOf(
//        GroupTag(1,"series/movies"),
//        GroupTag(2,"programming"),
//        GroupTag(3,"travelling"),
//        GroupTag(4,"games"),
//        GroupTag(5,"sport")
//    )

        var collection: MutableList<GroupTag> by
        Delegates.observable(mutableListOf()) { _, _, _ -> notifyDataSetChanged() }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_tag_item, parent, false)
            return TagsHolder(
                view
            )
        }

        override fun onBindViewHolder(holder: TagsHolder, position: Int) =
            holder.bind(collection[position])

        override fun getItemCount() = collection.size


        class TagsHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

            fun bind(tag: GroupTag) {
                itemView.tagCheckbox.text = tag.tag
            }
        }
    }


