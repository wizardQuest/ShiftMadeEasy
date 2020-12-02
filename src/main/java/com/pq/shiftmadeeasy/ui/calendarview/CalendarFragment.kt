package com.pq.shiftmadeeasy.ui.calendarview

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.RotateAnimation
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.pq.focuslist.base.gone
import com.pq.focuslist.base.visible
import com.pq.shiftmadeeasy.R
import com.pq.shiftmadeeasy.adapter.shifts.ShiftListAdapter
import com.pq.shiftmadeeasy.base.BaseFragment
import com.pq.shiftmadeeasy.localdatabase.calendarwithshift.CustomCalendar
import com.pq.shiftmadeeasy.localdatabase.calendarwithshift.CustomCalendarForRepeatingShifts
import com.pq.shiftmadeeasy.localdatabase.shift.Shift
import com.pq.shiftmadeeasy.ui.dialogs.AddNewShiftDialogFragment
import com.pq.shiftmadeeasy.ui.dialogs.SetShiftRepeatPatternDialogFragment
import com.pq.shiftmadeeasy.ui.dialogs.ShiftSelectorDialogFragment
import com.pq.shiftmadeeasy.ui.dialogs.ShiftSelectorDialogFragment.Companion.SHIFT_SELECTED_DATA
import com.pq.shiftmadeeasy.ui.dialogs.ShiftSelectorDialogFragment.Companion.SHIFT_SELECTED_DATE
import com.pq.shiftmadeeasy.ui.viewmodels.ViewModelProviderFactory
import com.pq.shiftmadeeasy.util.*
import com.pq.shiftmadeeasy.util.UtilConstants.DATE_PRIMARY_FORMAT
import com.pq.shiftmadeeasy.util.itemtouchhelper.RecyclerViewItemTouchHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.calendar_fragment.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CalendarFragment :
    BaseFragment(),
    ShiftListAdapter.Interaction,
    OnDayClickListener {

    private var revealShiftList = false
    private var updateShiftListFlag = true
    private var totalProjects = 0
    private val events = ArrayList<EventDay>()
    private lateinit var shiftList: MutableList<Shift>
    private lateinit var calendarList: MutableList<CustomCalendarForRepeatingShifts>
    private lateinit var calendarListCustomCalendar: MutableList<CustomCalendar>

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory
    private val shiftAndCalendarRepositoryViewModel: ShiftAndCalendarRepositoryViewModel by viewModels {
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
        // activity?.showToolbarAndHideBottomBar("Calendar")
        /*  toolbarLayout?.backButtonId?.setOnClickListener {
              activity?.onBackPressed()
          }*/
        bottomNavigationNotRequired()
        setCalendarClickListeners()
        setShiftListAdapter()
        shiftAndCalendarRepositoryViewModel.getAllShifts().observe(viewLifecycleOwner, { list ->
            if (updateShiftListFlag) {
                shiftListAdapter.submitList(getShiftListInOrder(list))
                updateShiftListFlag = false
            }
            shiftList = list
            setCalendar()
            setCustomCalendarList()
        })
        setClickListeners()
        setCalendarListeners()
        //calendarViewId?.setBackgroundColor(Colors.BRIGHT_GREEN)
    }

    private fun setCustomCalendarList() {
        shiftAndCalendarRepositoryViewModel.getAllCalendars()
            .observe(viewLifecycleOwner, { customList ->
                calendarListCustomCalendar = customList
            })
    }

    private fun setCalendarListeners() {
        /*val onNextMonthClickListener: OnCalendarPageChangeListener = object : OnCalendarPageChangeListener {
                override fun onChange() {
                    {
                        //TODO
                    }
                }
            }
        val onPreviousMonthClickListener: OnCalendarPageChangeListener = object : OnCalendarPageChangeListener {
            override fun onChange() {
                {
                        //TODO
                }
            }
        }
        calendarView.setOnForwardPageChangeListener(onNextMonthClickListener)
        calendarView.setOnPreviousPageChangeListener(onPreviousMonthClickListener)*/
    }

    private fun setCalendar() {
        shiftAndCalendarRepositoryViewModel.getAllCalendarsForRepeatingShifts()
            .observe(viewLifecycleOwner, { list ->
                calendarList = list
                if (list.isNotEmpty()) {
                    events.removeAll(events)
                    var rangeTimeInMilliseconds =
                        shiftAndCalendarRepositoryViewModel.calculateShiftRange(
                            list.first(),
                            list.last()
                        )
                    list.sortBy { it.calendarTime }
                    //Sets events for dates prior to range of CustomCalendarForRepeatingPattern stored in DB
                    setPreviousMonthCalendarEvents(calendarView.currentPageDate, list, rangeTimeInMilliseconds)
                    //Sets events for dates after range of CustomCalendarForRepeatingPattern stored in DB
                    setNextMonthCalendarEvents(calendarView.currentPageDate, list, rangeTimeInMilliseconds)
                    //Sets events for dates in range of CustomCalendarForRepeatingPattern stored in DB
                    setRangeCustomCalendarDates(list)
                    calendarView.setEvents(events)
                }
            })
    }

    private fun setRangeCustomCalendarDates(list: MutableList<CustomCalendarForRepeatingShifts>) {
        list.forEach {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it.calendarTime
            val filteredShiftList = shiftList.filter { shift ->
                shift.shiftId == it.shiftId
            }
            addEvent(calendar, filteredShiftList.first())
        }
    }

    private fun setNextMonthCalendarEvents(
        currentPageDate: Calendar,
        list: MutableList<CustomCalendarForRepeatingShifts>,
        rangeTimeInMilliseconds: Long
    ) {
        val firstCalendarDateFromRange = getCalendarDateFromMilliseconds(list.last().calendarTime)
        var dateAfterRangeInMilli = list.last().calendarTime + 86400000
        // var list = list.reversed()
        for (index in 25 until 32) {
            var timeDiffWithFirstShift = dateAfterRangeInMilli - list.last().calendarTime
            var remainderTimeInDays =
                getTimeAsDay(timeDiffWithFirstShift.rem(rangeTimeInMilliseconds))
            if (remainderTimeInDays == 0L) {
                remainderTimeInDays = getTimeAsDay(rangeTimeInMilliseconds)//cyclic
            }
            var customCalendar = list[remainderTimeInDays.toInt()-1]
            val calendar = getCalendarDateFromMilliseconds(dateAfterRangeInMilli)
            val filteredShiftList = shiftList.filter { shift ->
                shift.shiftId == customCalendar.shiftId
            }
            addEvent(calendar, filteredShiftList.first())
            dateAfterRangeInMilli = getNextDay(dateAfterRangeInMilli)
        }
    }

    private fun setPreviousMonthCalendarEvents(
        currentPageDate: Calendar,
        list: MutableList<CustomCalendarForRepeatingShifts>,
        rangeTimeInMilliseconds: Long
    ) {
        val firstCalendarDateFromRange = getCalendarDateFromMilliseconds(list.first().calendarTime)
        var list = list.reversed()
        for (index in 0 until firstCalendarDateFromRange.get(Calendar.DAY_OF_MONTH) - 1) {
            var timeDiffWithFirstShift = list.last().calendarTime - currentPageDate.timeInMillis
            var remainderTimeInDays =
                getTimeAsDay(timeDiffWithFirstShift.rem(rangeTimeInMilliseconds))
            if (remainderTimeInDays == 0L) {
                remainderTimeInDays = getTimeAsDay(rangeTimeInMilliseconds)//cyclic
            }
            var customCalendar = list[remainderTimeInDays.toInt() - 1]
            val calendar = getCalendarDateFromMilliseconds(currentPageDate.timeInMillis)
            val filteredShiftList = shiftList.filter { shift ->
                shift.shiftId == customCalendar.shiftId
            }
            addEvent(calendar, filteredShiftList.first())
            currentPageDate.timeInMillis = getNextDay(currentPageDate.timeInMillis)
        }
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
        minimizeAndMaximizeIconId?.setOnClickListener {
            openRangePicker()
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
                        /*var isSelectedDateHasEvent = false
                        var calendarEvent: EventDay? = null*/
                        calendar.time = convertedDate
                        /* events.forEach { it ->
                             if (it.calendar == calendar) {
                                 // events.remove(it)
                                 calendarEvent = it
                                 isSelectedDateHasEvent = true
                                 return@forEach
                             }
                         }*/
                        shiftAndCalendarRepositoryViewModel?.apply {
                            deleteConflictingCalendarIfPresent(
                                calendar.timeInMillis,
                                calendarListCustomCalendar
                            )
                            insert(
                                CustomCalendar(
                                    calendarTime = calendar.timeInMillis,
                                    shiftId = selectedShift.shiftId
                                )
                            )
                        }
                        /*if (isSelectedDateHasEvent) {
                            events.remove(calendarEvent)
                        }*/
                        /*addEvent(calendar, selectedShift)
                        calendarView.setEvents(events)*/
                    }
                }
            }
        }
    }


    private fun openRangePicker() {
        val dialogFragment = SetShiftRepeatPatternDialogFragment.newInstance()
        dialogFragment.setTargetFragment(this, SET_SHIFT_TIMELINE_DIALOG_REQUEST_CODE)
        dialogFragment.show(parentFragmentManager, SET_SHIFT_TIMELINE_DIALOG)
        /* *//*val min = Calendar.getInstance()
        min.add(Calendar.DAY_OF_MONTH, -5)
        val max = Calendar.getInstance()
        max.add(Calendar.DAY_OF_MONTH, 3)
        val selectedDays: MutableList<Calendar> = ArrayList()
        selectedDays.add(min)
        selectedDays.addAll(min.getDatesRange(max))
        selectedDays.add(max)*//*
        val rangeBuilder = DatePickerBuilder(requireContext(), this)
            .pickerType(CalendarView.RANGE_PICKER)
            .headerColor(R.color.black)
            .abbreviationsBarColor(R.color.white)
            .abbreviationsLabelsColor(android.R.color.white)
            .pagesColor(R.color.black)
            .selectionColor(android.R.color.white)
            .selectionLabelColor(R.color.black)
            .todayLabelColor(R.color.colorAccent)
            .dialogButtonsColor(android.R.color.white)
            .daysLabelsColor(android.R.color.white)
            .anotherMonthsDaysLabelsColor(R.color.black)
            .events(events)
            //.selectedDays(selectedDays)
            .maximumDaysRange(10)
        val rangePicker = rangeBuilder.build()
        rangePicker.show()*/
    }

    private fun addEvent(calendar: Calendar, shift: Shift) {
        val drawable = getTextInsideCircleDrawable(
            requireContext(),
            shift?.shiftShortForm,
            shift?.shiftColor
        )
        events.add(EventDay(calendar, drawable))
    }

    companion object {
        const val SHIFT_ADD_DIALOG = "PROJECT_ADD_DIALOG"
        const val SHIFT_DELETE_CONFIRMATION_DIALOG = "PROJECT_DELETE_CONFIRMATION_DIALOG"
        const val SHIFT_SELECTOR_DIALOG = "SHIFT_SELECTOR_DIALOG"
        const val SET_SHIFT_TIMELINE_DIALOG = "SET_SHIFT_TIMELINE_DIALOG"

        const val SHIFT_ADD_DIALOG_REQUEST_CODE = 1
        const val SHIFT_DELETE_DIALOG_REQUEST_CODE = 2

        const val SHIFT_SELECTOR_DIALOG_REQUEST_CODE = 3
        const val SET_SHIFT_TIMELINE_DIALOG_REQUEST_CODE = 4
        fun newInstance() = CalendarFragment()
    }

    override fun onDayClick(eventDay: EventDay) {
        val sdf = SimpleDateFormat(DATE_PRIMARY_FORMAT)
        val formattedDate = sdf.format(eventDay.calendar.time)
        val dialogFragment = ShiftSelectorDialogFragment.newInstance(formattedDate)
        dialogFragment.setTargetFragment(this, SHIFT_SELECTOR_DIALOG_REQUEST_CODE)
        dialogFragment.show(parentFragmentManager, SHIFT_SELECTOR_DIALOG)
    }
}