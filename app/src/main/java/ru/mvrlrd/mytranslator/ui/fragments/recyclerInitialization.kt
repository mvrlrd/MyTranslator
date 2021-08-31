package ru.mvrlrd.mytranslator.ui.fragments

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.mvrlrd.mytranslator.ui.old.old.ItemTouchHelperAdapter
import ru.mvrlrd.mytranslator.ui.old.old.SimpleItemTouchHelperCallback
import kotlin.math.max
import kotlin.math.min

private lateinit var callback: ItemTouchHelper.Callback

fun  <T : RecyclerView.ViewHolder?> initRecycler(
    _recyclerView: RecyclerView,
    _context: Context,
    _adapter: RecyclerView.Adapter<T>
) =
    _recyclerView.apply {
        layoutManager =
            LinearLayoutManager(_context)
        adapter =
            _adapter
    }
//            .addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))


//keep a distance between header("add new category") and the recycler items while scrolling
fun keepDistanceBtwHeaderAndRecyclerItemsWhileScrolling(
    _recyclerView: RecyclerView,
    _view: View
) {
    var mScrollY = 0F
    _recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(rcv: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(rcv, dx, dy)
            mScrollY += dy.toFloat()
            mScrollY = max(mScrollY, 0F)
            _view.translationY = min(-mScrollY, 0F)
        }
    })
}

fun attachCallbackToRecycler(_recyclerView: RecyclerView) {
    callback =
        SimpleItemTouchHelperCallback(
            _recyclerView.adapter as ItemTouchHelperAdapter
        )
    ItemTouchHelper(callback).attachToRecyclerView(_recyclerView)
}