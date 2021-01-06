package com.pq.shiftmadeeasy.ui.calendarview

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.applandeo.materialcalendarview.EventDay
import com.pq.shiftmadeeasy.data.EventDayLocal
import com.pq.shiftmadeeasy.localdatabase.calendarwithshift.CustomCalendarForRepeatingShifts
import com.pq.shiftmadeeasy.repository.calendar.CalendarRepositoryImpl
import com.pq.shiftmadeeasy.repository.shift.ShiftRepositoryImpl
import com.pq.shiftmadeeasy.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CalendarViewModel @Inject constructor(
    application: Application,
    private val shiftRepository: ShiftRepositoryImpl,
    private val calendarRepository: CalendarRepositoryImpl
) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    var allCalendarsForRepeatingShifts =
        calendarRepository.getAllCalendarsForRepeatingShifts().asLiveData()
    var allShifts = shiftRepository.getAllShifts().asLiveData()
    var currentMonthFirstCalendarLD: MutableLiveData<Calendar> = MutableLiveData()
    lateinit var currentMonthFirstCalendar: Calendar
    private var listOfMonthsAlreadySet: MutableList<Calendar> = ArrayList()
    private var shiftList = allShifts.value
    private var rangeTimeInMilliseconds: Long = 0L
    private var calendarList: MutableList<CustomCalendarForRepeatingShifts>? = null
    var events: MediatorLiveData<ArrayList<EventDayLocal>> = MediatorLiveData()
    var observableEvents: MediatorLiveData<ArrayList<EventDay>> = MediatorLiveData()

    //MutableStateFlow<ArrayList<EventDay>>(ArrayList()).asLiveData() as MediatorLiveData<ArrayList<EventDay>>
    var shiftCalculatorJob = Job()

    init {
        events.apply {
            this.value = ArrayList()
            val setCalendar: () -> Unit = {
                try {
                    setCalendar1()
                } catch (exception: Exception) {
                    Log.v(exception.message, " exception occurred")
                }
            }
            addSource(allShifts) { setCalendar.invoke() }
            addSource(allCalendarsForRepeatingShifts) { setCalendar.invoke() }
            addSource(currentMonthFirstCalendarLD) {
                currentMonthFirstCalendarLD.value?.let {
                    currentMonthFirstCalendar = it
                }
                setCalendar.invoke()
            }
        }
        observableEvents.apply {
            addSource(events) {
                val events = it.transformEvents()
                if(events != null)
                    value = events
            }
        }
    }

    private fun setCalendar1() {
        CoroutineScope(Dispatchers.IO + shiftCalculatorJob).launch {
            //don't execute until all data not received so return
            if (allShifts.value.isNullOrEmpty() || allCalendarsForRepeatingShifts.value.isNullOrEmpty() || currentMonthFirstCalendarLD.value == null)
                return@launch
            calendarList = allCalendarsForRepeatingShifts.value
            var list = allCalendarsForRepeatingShifts.value
            if (!list.isNullOrEmpty()) {
                list?.let {
                    rangeTimeInMilliseconds = calculateShiftRange(it.first(), it.last())
                    it.sortBy { it1 -> it1.calendarTime }
                    //setPreviousMonthCalendarEvents()
                    setPreviousMonthCalendarEvents1(
                        currentMonthFirstCalendar.getNewCalendarReplica { it1 ->
                            it1.add(Calendar.MONTH, -1)
                            it1.set(Calendar.DAY_OF_MONTH, 1)
                        },
                        it,
                        rangeTimeInMilliseconds
                    )
                    //set next month Calendar Events
                    setNextMonthCalendarEvents1(
                        currentMonthFirstCalendar.getNewCalendarReplica { it1 ->
                            it1.add(Calendar.MONTH, 1)
                            it1.set(Calendar.DAY_OF_MONTH, 1)
                        },
                        it,
                        rangeTimeInMilliseconds
                    )
                    setCurrentMonthCalendarEvents1(
                        currentMonthFirstCalendar.getNewCalendarReplica { it1 ->
                            it1.set(Calendar.DAY_OF_MONTH, 1)
                        },
                        it,
                        rangeTimeInMilliseconds
                    )
                    //observableEvents.value = events.value
                    events.postValue(events.value)
                }
            }
        }
    }

    private suspend fun setCurrentMonthCalendarEvents1(
        currentCalendar: Calendar,
        list: MutableList<CustomCalendarForRepeatingShifts>,
        rangeTimeInMilliseconds: Long
    ) {
        //TODO:If This month is already set return straight away
        if (isMonthAlreadySet(currentCalendar))
            return
        if (isThisMonthSameAsFirstRepeatingShiftMonth(currentCalendar, list)) {
            //TODO:Calculate last index which will be one prior to first from list
            var calendar = list.first().calendarTime.getCalendarFromTimeInMilli()
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            setPreviousMonthCalendarEvents1(
                getNewCalendarReplica(calendar),
                list,
                rangeTimeInMilliseconds,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }
        when {
            isThisMonthSameAsLastRepeatingShiftMonth(currentCalendar, list) -> {
                //TODO:Calculate first index
                val calendar = list.last().calendarTime.getCalendarFromTimeInMilli()
                calendar.add(Calendar.DAY_OF_MONTH, 1)
                setNextMonthCalendarEvents1(
                    getNewCalendarReplica(calendar),
                    list,
                    rangeTimeInMilliseconds,
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
            }
            list.first().calendarTime > currentCalendar.timeInMillis -> {
                setPreviousMonthCalendarEvents1(currentCalendar, list, rangeTimeInMilliseconds)
            }
            list.last().calendarTime < currentCalendar.timeInMillis -> {
                setNextMonthCalendarEvents1(currentCalendar, list, rangeTimeInMilliseconds)
            }
        }
        listOfMonthsAlreadySet.add(currentCalendar)
    }


    /*private fun setCalendar() {
        //don't execute until all data not received so return
        if (allShifts.value.isNullOrEmpty() || allCalendarsForRepeatingShifts.value.isNullOrEmpty() || currentMonthFirstCalendarLD.value == null)
            return
        calendarList = allCalendarsForRepeatingShifts.value
        var list = allCalendarsForRepeatingShifts.value
        list?.let {
            if (it.isNotEmpty()) {
                // events.removeAll(events)
                rangeTimeInMilliseconds =
                    calculateShiftRange(
                        it.first(),
                        it.last()
                    )
                it.sortBy { it1 -> it1.calendarTime }
                //set previous month Calendar Events
                setPreviousMonthCalendarEvents(
                    currentMonthFirstCalendar,
                    it,
                    rangeTimeInMilliseconds
                )
                //set next month Calendar Events
                setNextMonthCalendarEvents(
                    currentMonthFirstCalendar,
                    it,
                    rangeTimeInMilliseconds
                )
                //Sets events for dates prior to range of CustomCalendarForRepeatingPattern stored in DB
                listOfMonthsAlreadySet.add(currentMonthFirstCalendar)
                setPreviousCalendarEventsForCurrentMonth(
                    currentMonthFirstCalendar,
                    it,
                    rangeTimeInMilliseconds
                )
                //Sets events for dates after range of CustomCalendarForRepeatingPattern stored in DB
                setNextCalendarEventsForCurrentMonth(
                    currentMonthFirstCalendar,
                    it,
                    rangeTimeInMilliseconds
                )
                //Sets events for dates in range of CustomCalendarForRepeatingPattern stored in DB
                setRangeCustomCalendarDates(it)
                //calendarView.setEvents(events)
            }
        }
    }*/

    private suspend fun setNextMonthCalendarEvents(
        currentPageDate: Calendar,
        list: MutableList<CustomCalendarForRepeatingShifts>,
        rangeTimeInMilliseconds: Long
    ) {
        var nextCalendar = Calendar.getInstance()
        nextCalendar.timeInMillis = currentPageDate.timeInMillis
        nextCalendar.add(Calendar.MONTH, +1)
        nextCalendar.set(Calendar.DAY_OF_MONTH, 1)
        val lastDateOfNextMoth = nextCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        var startingIndex = 0
        for (index in 0 until lastDateOfNextMoth) {
            var timeDiffWithFirstShift = nextCalendar.timeInMillis - list.last().calendarTime
            var remainderTimeInDays =
                getTimeAsDay(timeDiffWithFirstShift.rem(rangeTimeInMilliseconds))
            if (remainderTimeInDays == 0L) {
                remainderTimeInDays = getTimeAsDay(rangeTimeInMilliseconds)//cyclic
            }
            var customCalendar = list[remainderTimeInDays.toInt() - 1]
            val calendar = getCalendarDateFromMilliseconds(nextCalendar.timeInMillis)
            addEvent(calendar, customCalendar)
            nextCalendar.timeInMillis = getNextDay(nextCalendar.timeInMillis)
        }
        listOfMonthsAlreadySet.add(currentPageDate)
        //nextCalendar.timeInMillis -8400000
    }

    private suspend fun setNextMonthCalendarEvents1(
        nextCalendar: Calendar,
        list: MutableList<CustomCalendarForRepeatingShifts>,
        rangeTimeInMilliseconds: Long,
        firstIndex: Int? = null
    ) {
        //TODO:If This month is already set return straight away
        if (isMonthAlreadySet(nextCalendar))
            return
        val lastDateOfNextMoth = nextCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        var startingIndex = firstIndex
        if (isThisMonthSameAsLastRepeatingShiftMonth(nextCalendar, list) && startingIndex == null) {
            setCurrentMonthCalendarEvents1(nextCalendar, list, rangeTimeInMilliseconds)
            return
            //startingIndex = getNewCalendarReplica(list.last().calendarTime).get(Calendar.DAY_OF_MONTH)
        }
        startingIndex = startingIndex ?: 0
        for (index in startingIndex until lastDateOfNextMoth) {
            var timeDiffWithFirstShift = nextCalendar.timeInMillis - list.last().calendarTime
            var remainderTimeInDays =
                getTimeAsDay(timeDiffWithFirstShift.rem(rangeTimeInMilliseconds))
            if (remainderTimeInDays == 0L) {
                remainderTimeInDays = getTimeAsDay(rangeTimeInMilliseconds)//cyclic
            }
            var customCalendar = list[remainderTimeInDays.toInt() - 1]
            val calendar = getCalendarDateFromMilliseconds(nextCalendar.timeInMillis)
            addEvent(calendar, customCalendar)
            nextCalendar.timeInMillis = getNextDay(nextCalendar.timeInMillis)
        }
        if (firstIndex == null) {
            listOfMonthsAlreadySet.add(nextCalendar.getNewCalendarReplica {
                it.add(Calendar.MONTH, -1)
            })
        }
        //nextCalendar.timeInMillis -8400000
    }

    private suspend fun setPreviousMonthCalendarEvents1(
        previousCalendar: Calendar,
        list: MutableList<CustomCalendarForRepeatingShifts>,
        rangeTimeInMilliseconds: Long,
        lastIndex: Int? = null
    ) {
        //TODO:If This month is already set return straight away
        if (isMonthAlreadySet(previousCalendar))
            return
        previousCalendar.set(Calendar.DAY_OF_MONTH, 1)
        var lastDateOfPreviousMonth: Int? = lastIndex
        if (isThisMonthSameAsFirstRepeatingShiftMonth(
                previousCalendar,
                list
            ) && lastIndex == null
        ) {
            setCurrentMonthCalendarEvents1(previousCalendar, list, rangeTimeInMilliseconds)
            return
            //lastDateOfPreviousMonth = getNewCalendarReplica(list.first().calendarTime).get(Calendar.DAY_OF_MONTH)
        }
        lastDateOfPreviousMonth =
            lastDateOfPreviousMonth ?: previousCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        //val firstCalendarDateFromRange = getCalendarDateFromMilliseconds(list.first().calendarTime)
        var list = list.reversed()
        for (index in 0 until lastDateOfPreviousMonth) {
            var timeDiffWithFirstShift =
                list.last().calendarTime - previousCalendar.timeInMillis //+ 86400000
            var remainderTimeInDays =
                getTimeAsDay(timeDiffWithFirstShift.rem(rangeTimeInMilliseconds))
            if (remainderTimeInDays == 0L) {
                remainderTimeInDays = getTimeAsDay(rangeTimeInMilliseconds)//cyclic
            }
            if (remainderTimeInDays.toInt() == -1) {
                Log.v("previous", remainderTimeInDays.toString())
            }
            Log.v("previous", remainderTimeInDays.toString())
            var customCalendar = list[remainderTimeInDays.toInt() - 1]
            val calendar = getCalendarDateFromMilliseconds(previousCalendar.timeInMillis)
            addEvent(calendar, customCalendar)
            previousCalendar.timeInMillis = getNextDay(previousCalendar.timeInMillis)
        }
        if (lastIndex == null) {
            listOfMonthsAlreadySet.add(previousCalendar.getNewCalendarReplica {
                it.add(Calendar.MONTH, -1)
            })
        }
    }

    private suspend fun setRangeCustomCalendarDates(list: MutableList<CustomCalendarForRepeatingShifts>) {
        list.forEach {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it.calendarTime
            addEvent(calendar, it)
        }
    }

    private suspend fun isThisMonthSameAsFirstRepeatingShiftMonth(
        previousCalendar: Calendar,
        list: MutableList<CustomCalendarForRepeatingShifts>
    ): Boolean {
        val calendar = getNewCalendarReplica(list.first().calendarTime)
        return (calendar.get(Calendar.MONTH) == previousCalendar.get(Calendar.MONTH))
    }

    private suspend fun isThisMonthSameAsLastRepeatingShiftMonth(
        previousCalendar: Calendar,
        list: MutableList<CustomCalendarForRepeatingShifts>
    ): Boolean {
        val calendar = getNewCalendarReplica(list.last().calendarTime)
        return (calendar.get(Calendar.MONTH) == previousCalendar.get(Calendar.MONTH))
    }

    private suspend fun setPreviousMonthCalendarEvents(
        currentPageDate: Calendar,
        list: MutableList<CustomCalendarForRepeatingShifts>,
        rangeTimeInMilliseconds: Long
    ) {
        var previousCalendar = Calendar.getInstance()
        previousCalendar.timeInMillis = currentPageDate.timeInMillis
        previousCalendar.add(Calendar.MONTH, -1)
        previousCalendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDateOfPreviousMonth = previousCalendar.time
        val lastDateOfPreviousMonth = previousCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        //val firstCalendarDateFromRange = getCalendarDateFromMilliseconds(list.first().calendarTime)
        var list = list.reversed()
        for (index in 0 until lastDateOfPreviousMonth) {
            var timeDiffWithFirstShift =
                list.last().calendarTime - previousCalendar.timeInMillis + 86400000
            var remainderTimeInDays =
                getTimeAsDay(timeDiffWithFirstShift.rem(rangeTimeInMilliseconds))
            if (remainderTimeInDays == 0L) {
                remainderTimeInDays = getTimeAsDay(rangeTimeInMilliseconds)//cyclic
            }
            var customCalendar = list[remainderTimeInDays.toInt() - 1]
            val calendar = getCalendarDateFromMilliseconds(previousCalendar.timeInMillis)
            addEvent(calendar, customCalendar)
            previousCalendar.timeInMillis = getNextDay(previousCalendar.timeInMillis)
        }
        listOfMonthsAlreadySet.add(currentPageDate)
    }

    private suspend fun addEvent(
        calendar: Calendar,
        customCalendar: CustomCalendarForRepeatingShifts
    ) {
        val filteredShiftList = allShifts?.value?.filter { shift ->
            shift.shiftId == customCalendar.shiftId
        }
        filteredShiftList?.first()?.let {
            val drawable = getTextInsideCircleDrawable(
                context,
                it?.shiftShortForm,
                it?.shiftColor
            )

            events.value?.add(EventDayLocal(calendar, drawable))
        }
    }

    suspend fun calculateShiftRange(
        first: CustomCalendarForRepeatingShifts,
        last: CustomCalendarForRepeatingShifts
    ): Long {
        return last.calendarTime - first.calendarTime + 86400000 // 1 day is added since it gives time difference and because of that it gets 1 day less
    }

    /*fun onNextMonthClickListener() {
        var currentMonth = Calendar.getInstance()
        currentMonthFirstCalendar?.let {
            it.add(Calendar.MONTH, 1)
            currentMonth.timeInMillis = it.timeInMillis
        }
        if (isMonthAlreadySet(currentMonth))
            return
        calendarList?.let {
            setNextMonthCalendarEvents(
                currentMonth,
                it,
                rangeTimeInMilliseconds
            )
        }
    }

    fun onPreviousMonthClickListener() {
        var currentMonth = Calendar.getInstance()
        currentMonthFirstCalendar?.let {
            it.add(Calendar.MONTH, -1)
            currentMonth.timeInMillis = it.timeInMillis
        }
        if (isMonthAlreadySet(currentMonth))
            return
        calendarList?.let {
            setPreviousMonthCalendarEvents(
                currentMonth,
                it,
                rangeTimeInMilliseconds
            )
        }
    }*/

    /* private fun setPreviousCalendarEventsForCurrentMonth(
         currentPageDate: Calendar,
         list: MutableList<CustomCalendarForRepeatingShifts>,
         rangeTimeInMilliseconds: Long
     ) {
         val firstCalendarDateFromRange = getCalendarDateFromMilliseconds(list.first().calendarTime)
         var list = list.reversed()
         for (index in 0 until firstCalendarDateFromRange.get(Calendar.DAY_OF_MONTH) - 1) {
             Log.v("previous", index.toString())
             var timeDiffWithFirstShift = list.last().calendarTime - currentPageDate.timeInMillis
             var remainderTimeInDays =
                 getTimeAsDay(timeDiffWithFirstShift.rem(rangeTimeInMilliseconds))
             if (remainderTimeInDays == 0L) {
                 remainderTimeInDays = getTimeAsDay(rangeTimeInMilliseconds)//cyclic
             }
             var customCalendar = list[remainderTimeInDays.toInt() - 1]
             val calendar = getCalendarDateFromMilliseconds(currentPageDate.timeInMillis)
             addEvent(calendar, customCalendar)
             currentPageDate.timeInMillis = getNextDay(currentPageDate.timeInMillis)
         }
     }*/

    /*private fun setNextCalendarEventsForCurrentMonth(
        currentPageDate: Calendar,
        list: MutableList<CustomCalendarForRepeatingShifts>,
        rangeTimeInMilliseconds: Long
    ) {
        val firstCalendarDateFromRange = getCalendarDateFromMilliseconds(list.last().calendarTime)
        var dateAfterRangeInMilli = list.last().calendarTime + 86400000
        // var list = list.reversed()
        for (index in 25 until 32) {
            Log.v("after", index.toString())
            var timeDiffWithFirstShift = dateAfterRangeInMilli - list.last().calendarTime
            var remainderTimeInDays =
                getTimeAsDay(timeDiffWithFirstShift.rem(rangeTimeInMilliseconds))
            if (remainderTimeInDays == 0L) {
                remainderTimeInDays = getTimeAsDay(rangeTimeInMilliseconds)//cyclic
            }
            var customCalendar = list[remainderTimeInDays.toInt() - 1]
            val calendar = getCalendarDateFromMilliseconds(dateAfterRangeInMilli)
            addEvent(calendar, customCalendar)
            dateAfterRangeInMilli = getNextDay(dateAfterRangeInMilli)
        }
    }
*/
    private fun isMonthAlreadySet(
        currentMonthCalendar: Calendar
    ): Boolean {
        val currentCalendarMonth = getCalendarMonth(currentMonthCalendar)
        val currentCalendarYear = getCalendarYear(currentMonthCalendar)
        listOfMonthsAlreadySet.forEach {
            val calendarMonthOfListItem = getCalendarMonth(it)
            if (calendarMonthOfListItem == currentCalendarMonth) {
                val calendarYearOfListItem = getCalendarYear(it)
                if (calendarYearOfListItem == currentCalendarYear)
                    return true
            }
        }
        return false
    }

    override fun onCleared() {
        super.onCleared()
        this.shiftCalculatorJob.cancel()
    }
}

private fun ArrayList<EventDayLocal>.transformEvents(): ArrayList<EventDay>? {
    var events: ArrayList<EventDay> = ArrayList()
    var i= 0
    if (this.isEmpty())
        return null
    try {
        this.forEach {
            Log.v((++i).toString(), "in For each")
            events.add(EventDay(day = it.day, drawable = it.drawable))
        }
    }catch (exception:Exception){
        Log.v(exception.toString(),"Exception occurred")
    }
    return events
}