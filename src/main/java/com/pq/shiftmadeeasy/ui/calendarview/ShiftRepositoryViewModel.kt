package com.pq.shiftmadeeasy.ui.calendarview

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pq.shiftmadeeasy.localdatabase.shift.Shift
import com.pq.shiftmadeeasy.repository.shift.ShiftRepositoryImpl
import kotlinx.coroutines.launch
import javax.inject.Inject

//Used in CalendarFragment and AddNewShiftDialogFragment
class ShiftRepositoryViewModel @Inject constructor(
    private val shiftRepository: ShiftRepositoryImpl
) : ViewModel() {

    fun insert(shift: Shift) {
        viewModelScope.launch {
            shiftRepository.insert(shift)
        }
    }

    fun getAllShifts(): LiveData<MutableList<Shift>> {
        return shiftRepository.getAllShifts()
    }
}