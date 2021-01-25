package ru.mvrlrd.mytranslator.ui.recycler_tags

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_tag_item.view.*
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.GroupTag
import kotlin.properties.Delegates


class TagsAdapter(val onItemChecked: OnItemChecked) :
        RecyclerView.Adapter<TagsAdapter.TagsHolder>(){

        var collection: MutableList<GroupTag> by
        Delegates.observable(mutableListOf()) { _, _, _ -> notifyDataSetChanged() }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_tag_item, parent, false)
            return TagsHolder(
                view
            )
        }
// передаю список нажатых чекбоксов в транслэйшн фрагмент. надо будет связать эти тэги со словом через реф кросс

    override fun onBindViewHolder(holder: TagsHolder, position: Int) {
        holder.bind(collection[position])
        holder.itemView.tagCheckbox.setChecked(collection[position].isChecked)
        holder.itemView.tagCheckbox.setOnClickListener { view ->
            if (view is CheckBox) {
                if (collection[position].isChecked ){
                    onItemChecked.checkedList.add(collection[position])
                }else{
                    if (onItemChecked.checkedList.contains(collection[position])){
                        onItemChecked.checkedList.remove(collection[position])
                    }
                }
                collection[position].isChecked = view.isChecked
            }
        }
    }


        override fun getItemCount() = collection.size


        class TagsHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {


            fun bind(tag: GroupTag) {
                itemView.tagCheckbox.text = tag.tag


            }


        }
    }


