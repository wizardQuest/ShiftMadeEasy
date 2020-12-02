package com.pq.shiftmadeeasy.adapter.shifts

import android.view.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.pq.focuslist.util.itemtouchhelper.ItemTouchHelperAdapter
import com.pq.shiftmadeeasy.R
import com.pq.shiftmadeeasy.localdatabase.shift.Shift
import com.pq.shiftmadeeasy.util.getTextInsideCircleDrawable
import kotlinx.android.synthetic.main.shift_item_view.view.*

class ShiftListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ItemTouchHelperAdapter {

    private var shiftList: MutableList<Shift> = ArrayList()
    var itemTouchHelper: ItemTouchHelper? = null
    private var shiftToBeDeletedPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ShiftViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.shift_item_view, parent, false),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ShiftViewHolder -> {
                holder.bind(shiftList[position])
            }
        }
    }

    override fun getItemCount(): Int = shiftList.size

    fun submitList(list: List<Shift>) {
        shiftList = list.toMutableList()
        notifyDataSetChanged()
    }

    fun deleteShift(shouldShiftBeDeleted: Boolean) {
        if (shouldShiftBeDeleted) {
            shiftList.removeAt(shiftToBeDeletedPosition)
            notifyItemRemoved(shiftToBeDeletedPosition)
        } else {
            notifyDataSetChanged()
        }
    }

    fun setTouchHelper(itemTouchHelper: ItemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper
    }

    inner class ShiftViewHolder(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView),
        View.OnTouchListener,
        GestureDetector.OnGestureListener {
        //listeners
        private val gestureDetector = GestureDetector(itemView.context, this)

        //views
        private val shiftTitle = itemView.shiftTitleId
        private var shiftIcon = itemView.colorId
        fun bind(shift: Shift) {
            //touch listener attached
            itemView.setOnTouchListener(this)

            shiftTitle?.text = shift.shiftTitle
            /*shiftIcon?.run {
                mutate()
            }*/
            shiftIcon?.setImageDrawable(
                shift.shiftColor?.let {
                    getTextInsideCircleDrawable(
                        itemView.context,
                        shift.shiftShortForm,
                        it
                    )
                }
            )
        }

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            gestureDetector.onTouchEvent(event)
            return true
        }

        override fun onDown(e: MotionEvent?): Boolean = false

        override fun onShowPress(e: MotionEvent?) {
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            interaction?.onItemSelected(adapterPosition)
            return true
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean = true

        override fun onLongPress(e: MotionEvent?) {
            itemTouchHelper?.startDrag(this)
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean = true

    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        var fromShiftItem = shiftList[fromPosition]
        shiftList.remove(fromShiftItem)
        shiftList.add(toPosition, fromShiftItem)
        if (fromPosition < toPosition) {
            val fromPositionProjectIndex = shiftList[fromPosition].shiftIndex
            shiftList[fromPosition].shiftIndex = shiftList[toPosition].shiftIndex
            shiftList[toPosition].shiftIndex = fromPositionProjectIndex

        } else {
            val fromPositionProjectIndex = shiftList[fromPosition].shiftIndex
            shiftList[fromPosition].shiftIndex = shiftList[toPosition].shiftIndex
            shiftList[toPosition].shiftIndex = fromPositionProjectIndex
        }
        shiftList
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemSwipedToDelete(position: Int) {
        shiftList.removeAt(position)
        interaction?.onItemDeleted(shiftList[position])
        notifyItemRemoved(position)
    }

    override fun onItemSwipedToComplete(position: Int) {
        TODO("Not yet implemented")
    }

    override fun moveItemAfterMoveEventComplete() {
        interaction?.onItemMoved(shiftList)
    }

    interface Interaction {
        fun onItemSelected(position: Int)
        fun onItemDeleted(shift: Shift) {
        }

        fun onItemMoved(shiftListFromSequenceChanged: MutableList<Shift>) {
        }
    }
}