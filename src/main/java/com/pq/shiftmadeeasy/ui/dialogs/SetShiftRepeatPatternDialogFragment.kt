package com.pq.shiftmadeeasy.ui.dialogs

import android.graphics.Point
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.applandeo.materialcalendarview.EventDay
import com.pq.shiftmadeeasy.R
import com.pq.shiftmadeeasy.localdatabase.calendarwithshift.CustomCalendar
import com.pq.shiftmadeeasy.localdatabase.shift.Shift
import com.pq.shiftmadeeasy.ui.calendarview.ShiftAndCalendarRepositoryViewModel
import com.pq.shiftmadeeasy.ui.viewmodels.ViewModelProviderFactory
import com.pq.shiftmadeeasy.util.getTextInsideCircleDrawable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.set_shift_calendar_dialog_freagment.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SetShiftRepeatPatternDialogFragment : DialogFragment() {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory
    private val shiftAndCalendarRepositoryViewModel: ShiftAndCalendarRepositoryViewModel by viewModels {
        viewModelProviderFactory
    }
    private lateinit var shiftList: MutableList<Shift>
    private lateinit var calendarList: MutableList<CustomCalendar>
    private val events = ArrayList<EventDay>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.set_shift_calendar_dialog_freagment, container, false)
        //making background transparent for perfect round corners
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSpinnerAdapter()
        setListeners()
        shiftAndCalendarRepositoryViewModel.getAllShifts().observe(viewLifecycleOwner, { list ->
            shiftList = list
            setCalendar()
        })
    }

    private fun setListeners() {
        repeatShiftPatterButton?.setOnClickListener {
            shiftAndCalendarRepositoryViewModel.repeatShiftPatternClicked(calendarView?.selectedDates,calendarList)
        }
    }

    private fun setCalendar() {
        shiftAndCalendarRepositoryViewModel.getAllCalendars().observe(viewLifecycleOwner, { list ->
            calendarList = list
            list.forEach {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = it.calendarTime
                val filteredShiftList = shiftList.filter { shift ->
                    shift.shiftId == it.shiftId
                }
                addEvent(calendar, filteredShiftList.first())
            }
            calendarView.setEvents(events)
        })
    }

    private fun addEvent(calendar: Calendar, shift: Shift) {
        val drawable = getTextInsideCircleDrawable(
            requireContext(),
            shift?.shiftShortForm,
            shift?.shiftColor
        )
        events.add(EventDay(calendar, drawable))
    }

    private fun setSpinnerAdapter() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.timeline_options_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            setTimelineSelectorSpinnerId.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        //setting dialog width in percentage
        val window = dialog?.window
        val size = Point()

        val display = window?.windowManager?.defaultDisplay
        display?.getSize(size);

        val width = size.x;
        val height = size.y

        window?.setLayout(((width * 0.85).toInt()), ViewGroup.LayoutParams.WRAP_CONTENT);
        window?.setGravity(Gravity.CENTER)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    companion object {
        fun newInstance(): SetShiftRepeatPatternDialogFragment {
            var fragment = SetShiftRepeatPatternDialogFragment()
            return fragment
        }
    }
}