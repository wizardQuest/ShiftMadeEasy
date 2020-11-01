package com.pq.shiftmadeeasy.ui.calendarview

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.RotateAnimation
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.pq.focuslist.base.gone
import com.pq.focuslist.base.visible
import com.pq.shiftmadeeasy.R
import com.pq.shiftmadeeasy.adapter.shifts.ShiftListAdapter
import com.pq.shiftmadeeasy.base.showToolbarAndHideBottomBar
import com.pq.shiftmadeeasy.localdatabase.shift.Shift
import com.pq.shiftmadeeasy.ui.dialogs.AddNewShiftDialogFragment
import com.pq.shiftmadeeasy.ui.dialogs.ShiftSelectorDialogFragment
import com.pq.shiftmadeeasy.ui.dialogs.ShiftSelectorDialogFragment.Companion.SHIFT_SELECTED_DATA
import com.pq.shiftmadeeasy.ui.dialogs.ShiftSelectorDialogFragment.Companion.SHIFT_SELECTED_DATE
import com.pq.shiftmadeeasy.ui.viewmodels.ViewModelProviderFactory
import com.pq.shiftmadeeasy.util.TopSpacingDecorator
import com.pq.shiftmadeeasy.util.UtilConstants.DATE_PRIMARY_FORMAT
import com.pq.shiftmadeeasy.util.getRotationAnimation
import com.pq.shiftmadeeasy.util.getShiftListInOrder
import com.pq.shiftmadeeasy.util.getTextInsideCircleDrawable
import com.pq.shiftmadeeasy.util.itemtouchhelper.RecyclerViewItemTouchHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.calendar_fragment.*
import kotlinx.android.synthetic.main.toolbar.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CalendarFragment : Fragment(), ShiftListAdapter.Interaction, OnDayClickListener {

    private var revealShiftList = false
    private var updateShiftListFlag = true
    private var totalProjects = 0

    private val events = ArrayList<EventDay>()

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory
    private val shiftRepositoryViewModel: ShiftRepositoryViewModel by viewModels {
        viewModelProviderFactory
    }
    private lateinit var shiftListAdapter: ShiftListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.calendar_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showToolbarAndHideBottomBar("Calendar")
        toolbarLayout?.backButtonId?.setOnClickListener {
            activity?.onBackPressed()
        }
        setClickListeners()
        setCalendarClickListeners()
        setShiftListAdapter()
        shiftRepositoryViewModel.getAllShifts().observe(viewLifecycleOwner, { list ->
            if (updateShiftListFlag) {
                shiftListAdapter.submitList(getShiftListInOrder(list))
                updateShiftListFlag = false
            }
        })
        events.add(EventDay(Calendar.getInstance(), R.drawable.ic_baseline_settings))
        calendarView.setEvents(events)
        //calendarViewId?.setBackgroundColor(Colors.BRIGHT_GREEN)
    }

    private fun setCalendarClickListeners() {
        calendarView?.setOnDayClickListener(this@CalendarFragment)
    }

    private fun setShiftListAdapter() {
        shiftRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            val topSpacingDecorator = TopSpacingDecorator(30)
            addItemDecoration(topSpacingDecorator)
            shiftListAdapter = ShiftListAdapter(this@CalendarFragment)
            val callback = RecyclerViewItemTouchHelper(shiftListAdapter)
            val itemTouchHelper = ItemTouchHelper(callback)
            shiftListAdapter.setTouchHelper(itemTouchHelper)
            itemTouchHelper.attachToRecyclerView(this)
            adapter = shiftListAdapter
        }
    }

    private fun setClickListeners() {
        shiftMinAndMaxId?.setOnClickListener {
            iconAnimation(it)
            if (revealShiftList)
                shiftRecyclerView.visible()
            else
                shiftRecyclerView.gone()
        }
        addNewShiftButtonId?.setOnClickListener {
            val dialogFragment = AddNewShiftDialogFragment.newInstance()
            dialogFragment.setTargetFragment(this, SHIFT_ADD_DIALOG_REQUEST_CODE)
            dialogFragment.show(parentFragmentManager, SHIFT_ADD_DIALOG)
        }
    }

    private fun iconAnimation(it: View) {
        var animator: RotateAnimation?
        if (!revealShiftList) {
            animator = getRotationAnimation(0.0f, -1.0f * 180.0f)
            revealShiftList()
        } else {
            animator = getRotationAnimation(-1.0f * 180.0f, 0.0f)
            hideShiftList()
        }
        animator?.let { _ ->
            it.startAnimation(animator)
        }
    }

    private fun revealShiftList() {
        revealShiftList = true
        shiftRecyclerView.visible()
    }

    private fun hideShiftList() {
        revealShiftList = false
        shiftRecyclerView.gone()
    }

    override fun onItemSelected(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemDeleted(shift: Shift) {
        TODO("Not yet implemented")
    }

    override fun onItemMoved(shiftListFromSequenceChanged: MutableList<Shift>) {
        TODO("Not yet implemented")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SHIFT_SELECTOR_DIALOG_REQUEST_CODE -> {
                when (resultCode) {
                    ShiftSelectorDialogFragment.DAY_SELECTED_RESULT_CODE -> {
                        val bundleData = data?.getBundleExtra(SHIFT_SELECTED_DATA)
                        val selectedShift = bundleData?.get(SHIFT_SELECTED_DATA) as Shift
                        val selectedDate = bundleData?.get(SHIFT_SELECTED_DATE) as String
                        val sdf = SimpleDateFormat(DATE_PRIMARY_FORMAT)
                        val convertedDate = sdf.parse(selectedDate)
                        val calendar = Calendar.getInstance()
                        calendar.time = convertedDate
                        selectedShift?.apply {
                            context?.let {
                                events.add(
                                        EventDay(
                                                calendar,

                                                getTextInsideCircleDrawable(
                                                        it,
                                                        selectedShift?.shiftTitle,
                                                        selectedShift?.shiftColor
                                                 )

                                        )
                                )
                            }
                        }
                        events.add(EventDay(Calendar.getInstance(), R.drawable.ic_baseline_settings))
                        calendarView.setEvents(events)
                    }
                }
            }
        }
    }

    companion object {
        const val SHIFT_ADD_DIALOG = "PROJECT_ADD_DIALOG"
        const val SHIFT_DELETE_CONFIRMATION_DIALOG = "PROJECT_DELETE_CONFIRMATION_DIALOG"
        const val SHIFT_SELECTOR_DIALOG = "SHIFT_SELECTOR_DIALOG"

        const val SHIFT_ADD_DIALOG_REQUEST_CODE = 1
        const val SHIFT_DELETE_DIALOG_REQUEST_CODE = 2

        const val SHIFT_SELECTOR_DIALOG_REQUEST_CODE = 3
        fun newInstance() = CalendarFragment()
    }

    override fun onDayClick(eventDay: EventDay) {
        val sdf = SimpleDateFormat(DATE_PRIMARY_FORMAT)
        val formattedDate = sdf.format(eventDay.calendar.time)
        val dialogFragment = ShiftSelectorDialogFragment.newInstance(formattedDate)
        dialogFragment.setTargetFragment(this, SHIFT_SELECTOR_DIALOG_REQUEST_CODE)
        dialogFragment.show(parentFragmentManager, SHIFT_ADD_DIALOG)
    }
}