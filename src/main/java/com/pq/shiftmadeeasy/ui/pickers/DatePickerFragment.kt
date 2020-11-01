package com.pq.shiftmadeeasy.ui.pickers

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.google.android.material.transition.MaterialSharedAxis
import java.util.*

class DatePickerFragment(private val dateSetListener: DatePickerDialog.OnDateSetListener) :
    DialogFragment(), DatePickerDialog.OnDateSetListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //TODO:Make Transition
        enterTransition = MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.X, true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        // Create a new instance of DatePickerDialog and return it
        return context?.let { DatePickerDialog(it, dateSetListener, year, month, day) } as Dialog
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
    }
}
