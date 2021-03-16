package ru.mvrlrd.mytranslator.ui.recycler_tags

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_tag_item.view.*
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.Category
import kotlin.properties.Delegates


class TagsAdapter(private val onItemChecked: OnItemChecked) :
        RecyclerView.Adapter<TagsAdapter.TagsHolder>(){

        var allTagList: MutableList<Category> by
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
        holder.bind(allTagList[position])
//        holder.itemView.tagCheckbox.setChecked(allTagList[position].isChecked)

        ////////

        holder.itemView.tagCheckbox.setOnClickListener { view ->
            if (view is CheckBox) {
                if ((view.isChecked)&&(!onItemChecked._checkedList.contains(allTagList[position]))) {
                    println("+++++++++++++++++C H E C K E D ! ! !++++++++++++++++++++${view.text}")
                    onItemChecked._checkedList.add(allTagList[position])

                } else {
                    if ((onItemChecked._checkedList.contains(allTagList[position]))&&(!view.isChecked)) {
                        println("+++++++++++++++++UN C H E C K E D ! ! !++++++++++++++++++++${view.text}")
                        onItemChecked._checkedList.remove(allTagList[position])
                    }
                }
//                allTagList[position].isChecked = view.isChecked
            }
        }
    }


        override fun getItemCount() = allTagList.size


        class TagsHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {


            fun bind(tag: Category) {
                itemView.tagCheckbox.text = tag.name


            }


        }
    }


