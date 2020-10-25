package ru.mvrlrd.mytranslator.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.room.HistoryEntity

class MyAdapter(var list: List<HistoryEntity>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private val TAG = "MainAdapter"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//  https://medium.com/@noureldeen.abouelkassem/difference-between-position-getadapterposition-and-getlayoutposition-in-recyclerview-80279a2711d1
        //https://www.tutorialspoint.com/get-clicked-item-and-its-position-in-recyclerview
        //https://stackoverflow.com/questions/26682277/how-do-i-get-the-position-selected-in-a-recyclerview

//  https://medium.com/@noureldeen.abouelkassem/difference-between-position-getadapterposition-and-getlayoutposition-in-recyclerview-80279a2711d1
        //https://www.tutorialspoint.com/get-clicked-item-and-its-position-in-recyclerview
        //https://stackoverflow.com/questions/26682277/how-do-i-get-the-position-selected-in-a-recyclerview

            holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size


    class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recycler_item_history, parent, false)) {
        private var textView: TextView? = null
        init {
            textView = itemView.findViewById(R.id.text_view_recycler_history)
        }

        fun bind(historyEntity: HistoryEntity) {
            textView?.text = historyEntity.text
        }
    }
}

