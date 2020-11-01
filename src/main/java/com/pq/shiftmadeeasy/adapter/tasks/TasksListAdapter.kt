package com.pq.shiftmadeeasy.adapter.tasks

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.pq.focuslist.util.itemtouchhelper.ItemTouchHelperAdapter
import com.pq.shiftmadeeasy.R
import com.pq.shiftmadeeasy.localdatabase.shift.Shift
import com.pq.shiftmadeeasy.localdatabase.task.Task
import com.pq.shiftmadeeasy.util.getCumulativeHours
import kotlinx.android.synthetic.main.tasks_recycler_view.view.*

class TasksListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ItemTouchHelperAdapter {

    private var tasksList: MutableList<Task> = ArrayList()
    var itemTouchHelper: ItemTouchHelper? = null
    private var cumulativeHours: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TasksViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.tasks_recycler_view, parent, false),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TasksViewHolder -> {
                holder.bind(tasksList[position])
            }
        }
    }

    override fun getItemCount(): Int = tasksList.size

    fun submitList(list: List<Task>) {
        tasksList = list.toMutableList()
        cumulativeHours = "00:00 Hrs"
        notifyDataSetChanged()
    }

    fun setTouchHelper(itemTouchHelper: ItemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper
    }

    inner class TasksViewHolder(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView),
        View.OnTouchListener,
        GestureDetector.OnGestureListener {

        //listeners
        private val gestureDetector = GestureDetector(itemView.context, this)

        private val date: TextView = itemView.dateTextId
        private val fromTime: TextView = itemView.fromTimeId
        private val toTime: TextView = itemView.toTimeId
        private val taskedLoggedHours = itemView.taskLoggedHoursId
        private val cumulativeLoggedHours = itemView.cumulativeLoggedHoursId
        private val position = itemView.positionId

        fun bind(task: Task) {
            //touch listener attached
            itemView.setOnTouchListener(this)
            cumulativeHours = task.taskLoggedHours?.let {
                getCumulativeHours(
                    totalTime = cumulativeHours,
                    taskLoggedTime = it
                )
            } ?: "00:00 Hrs"
            date.text = task.taskDate
            fromTime.text = task.taskFromTime + " to "
            toTime.text = task.taskToTime
            taskedLoggedHours.text = "Duration: " + task.taskLoggedHours
            cumulativeLoggedHours.text = "Cumulative: $cumulativeHours"
            position.text = "Position: " + task.position.toString()
        }

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            gestureDetector.onTouchEvent(event)
            return true
        }

        override fun onShowPress(e: MotionEvent?) {
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            interaction?.onItemSelected(adapterPosition)
            return true
        }

        override fun onDown(e: MotionEvent?) = false

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean = false

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean = true

        override fun onLongPress(e: MotionEvent?) {
            itemTouchHelper?.startDrag(this)
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        var fromProject = tasksList[fromPosition]
        tasksList.remove(fromProject)
        tasksList.add(toPosition, fromProject)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemSwipedToDelete(position: Int) {
        tasksList.removeAt(position)
        interaction?.onItemDeleted(tasksList[position])
        notifyItemRemoved(position)
    }

    override fun onItemSwipedToComplete(position: Int) {
        TODO("Not yet implemented")
    }

    override fun moveItemAfterMoveEventComplete() {
        TODO("Not yet implemented")
    }


    interface Interaction {
        fun onItemSelected(position: Int)
        fun onItemDeleted(task: Task)
    }
}