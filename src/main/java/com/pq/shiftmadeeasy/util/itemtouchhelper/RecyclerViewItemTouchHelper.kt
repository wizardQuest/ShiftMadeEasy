package com.pq.shiftmadeeasy.util.itemtouchhelper

import android.content.Context
import android.util.TypedValue
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.pq.focuslist.util.itemtouchhelper.ItemTouchHelperAdapter

class RecyclerViewItemTouchHelper(itemTouchHelperAdapter: ItemTouchHelperAdapter) :
    ItemTouchHelper.Callback() {
    var adapter: ItemTouchHelperAdapter = itemTouchHelperAdapter
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags =
            ItemTouchHelper.UP or (ItemTouchHelper.DOWN) or (ItemTouchHelper.RIGHT) or (ItemTouchHelper.LEFT)
        val swipeFlags = ItemTouchHelper.START or (ItemTouchHelper.END)
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onItemSwipedToDelete(viewHolder.adapterPosition)
    }

    override fun isLongPressDragEnabled() = true

    override fun isItemViewSwipeEnabled() = true

    private fun getCurrentThemeColor(context: Context, attr: Int): Int {
        val typedValue = TypedValue()
        val theme = context?.theme
        theme?.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }
}