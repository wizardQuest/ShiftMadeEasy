package com.pq.shiftmadeeasy.ui.addtodobottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pq.shiftmadeeasy.data.Time
import com.pq.shiftmadeeasy.util.UtilConstants.DATE_PRIMARY_FORMAT
import com.pq.shiftmadeeasy.util.UtilConstants.DATE_SECONDARY_FORMAT
import com.pq.shiftmadeeasy.util.UtilConstants.WEEK_DAY_FETCHING_FORMAT
import com.pq.shiftmadeeasy.util.getMonthByName
import com.pq.shiftmadeeasy.util.getTimeDifference
import com.pq.shiftmadeeasy.util.getWeekByName
import com.pq.shiftmadeeasy.util.manipulateTimeToDisplay
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AddTodoTaskViewModel @Inject constructor() : ViewModel() {

    private var _date = MutableLiveData<String>()
    val date: LiveData<String>
        get() = _date

    private var _fromTime = MutableLiveData<String>()
    val fromTime: LiveData<String>
        get() = _fromTime

    private var _toTime = MutableLiveData<String>()
    val toTime: LiveData<String>
        get() = _toTime

    private var _taskLoggedHours = MutableLiveData<String>()
    val taskLoggedHours: LiveData<String>
        get() = _taskLoggedHours

    var calender: Calendar = Calendar.getInstance()

    private var _taskTimeStamp = MutableLiveData<Long>()
    val taskTimStamp: LiveData<Long>
        get() = _taskTimeStamp

    fun setDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        calender.set(Calendar.YEAR, year)
        calender.set(Calendar.MONTH, monthOfYear)
        calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val getWeekSdf = SimpleDateFormat(DATE_SECONDARY_FORMAT, Locale.getDefault())
        val weekSdf = SimpleDateFormat(WEEK_DAY_FETCHING_FORMAT, Locale.getDefault())
        val date = getWeekSdf.parse("$dayOfMonth-$monthOfYear-$year")
        val dayOfWeek = weekSdf.format(date)
        val monthByName = getMonthByName(monthOfYear)
        val weekByName = getWeekByName(dayOfWeek)
        val formattedDate = "$weekByName, $dayOfMonth $monthByName $year"
        setFromTimeStamp()
        _date.value = formattedDate
    }

    fun setFromTime(hourOfDay: Int, minute: Int) {
        val min = String.format("%02d", minute)
        val hour = String.format("%02d", hourOfDay)

        _fromTime.value = "$hour:$min Hrs"
        //Update task logged Hours
        calender.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calender.set(Calendar.MINUTE, minute)
        setTaskLoggedHours()
        setFromTimeStamp()
    }

    fun setToTime(hourOfDay: Int, minute: Int) {
        val min = String.format("%02d", minute)
        val hour = String.format("%02d", hourOfDay)
        _toTime.value = "$hour:$min Hrs"
        //Update task logged Hours
        setTaskLoggedHours()
    }

    private fun setTaskLoggedHours() {
        var time = Time(0, 0)
        _fromTime.value?.let {
            _toTime.value?.let { it1 ->
                time = getTimeDifference(
                    fromTime = it,
                    toTime = it1
                )
                _taskLoggedHours.value = manipulateTimeToDisplay(time)
            }
        }
    }

    private fun setFromTimeStamp() {
        _taskTimeStamp.value = calender.timeInMillis
    }

    fun setData() {
        setDateOnStart()
        setFromAndToTimeOnStart()
        setLoggedHoursOnStart()
    }

    private fun setLoggedHoursOnStart() {
        _taskLoggedHours.value = "00:00 Hrs"
    }

    private fun setFromAndToTimeOnStart() {
        val time = Calendar.getInstance().time
        val sdf = SimpleDateFormat("HH:MM", Locale.getDefault())
        val formattedTime = sdf.format(time)
        _fromTime.value = "$formattedTime Hrs"
        _toTime.value = "$formattedTime Hrs"
    }

    private fun setDateOnStart() {
        val date = Calendar.getInstance().time
        val sdf = SimpleDateFormat(DATE_PRIMARY_FORMAT, Locale.getDefault())
        val formattedDate = sdf.format(date)
        _date.value = formattedDate
    }
}