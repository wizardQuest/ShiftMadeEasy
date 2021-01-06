package com.pq.shiftmadeeasy.ui.calendarview

import androidx.lifecycle.*
import com.applandeo.materialcalendarview.EventDay
import com.pq.shiftmadeeasy.localdatabase.calendarwithshift.CustomCalendar
import com.pq.shiftmadeeasy.localdatabase.calendarwithshift.CustomCalendarForRepeatingShifts
import com.pq.shiftmadeeasy.localdatabase.shift.Shift
import com.pq.shiftmadeeasy.repository.calendar.CalendarRepositoryImpl
import com.pq.shiftmadeeasy.repository.shift.ShiftRepositoryImpl
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

//Used in CalendarFragment and AddNewShiftDialogFragment & SetShiftForCalendarDialogFragment
class ShiftAndCalendarRepositoryViewModel @Inject constructor(
    private val shiftRepository: ShiftRepositoryImpl,
    private val calendarRepository: CalendarRepositoryImpl
) : ViewModel() {

    private var _eventsList = MutableLiveData<List<EventDay>>()
    val eventsList: LiveData<List<EventDay>>
        get() = _eventsList

    var allCalendars = calendarRepository.getAllCalendars().asLiveData()

    var allCalendarsForRepeatingShifts = calendarRepository.getAllCalendarsForRepeatingShifts().asLiveData()

    var allShifts= shiftRepository.getAllShifts().asLiveData()

    fun insert(shift: Shift) {
        viewModelScope.launch {
            shiftRepository.insert(shift)
        }
    }

    fun insert(calendar: CustomCalendar) {
        viewModelScope.launch {
            calendarRepository.insert(calendar)
        }
    }

    private fun updateCalendarForRepeatingShifts(calendarList: List<CustomCalendarForRepeatingShifts>) {
        viewModelScope.launch {
            calendarRepository.updateCalendarForRepeatingShifts(calendarList)
        }
    }

    fun deleteConflictingCalendarIfPresent(
        timeInMillis: Long,
        calendarList: MutableList<CustomCalendar>
    ) {
        val filteredCalendar = calendarList?.filter { it.calendarTime == timeInMillis }
        filteredCalendar.forEach {
            viewModelScope.launch {
                calendarRepository.delete(it)
            }
        }
    }

    fun repeatShiftPatternClicked(
        selectedDates: List<Calendar>?,
        calendarList: MutableList<CustomCalendar>
    ) {
        selectedDates?.let { shiftPatternList ->
            viewModelScope.launch {
                var newCalendarList: MutableList<CustomCalendarForRepeatingShifts> = ArrayList()
                for (element in shiftPatternList) {
                    var filteredCalendar = calendarList.filter { calendar ->
                        calendar.calendarTime == element.timeInMillis
                    }
                    newCalendarList?.add(
                        mapCustomCalendarToRepeatingCalendar(filteredCalendar.first())
                    )
                }
                newCalendarList?.let {
                    updateCalendarForRepeatingShifts(newCalendarList)
                }
            }
        }
    }

    private fun mapCustomCalendarToRepeatingCalendar(customCalendar: CustomCalendar): CustomCalendarForRepeatingShifts {
        return CustomCalendarForRepeatingShifts(
            calendarTime = customCalendar.calendarTime,
            shiftId = customCalendar.shiftId
        )
    }
}