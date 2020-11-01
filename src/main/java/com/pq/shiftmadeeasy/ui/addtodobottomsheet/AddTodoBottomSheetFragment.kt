package com.pq.shiftmadeeasy.ui.addtodobottomsheet

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pq.shiftmadeeasy.R
import com.pq.shiftmadeeasy.databinding.FragmentAddTodoBottomSheetBinding
import com.pq.shiftmadeeasy.localdatabase.task.Task
import com.pq.shiftmadeeasy.ui.pickers.DatePickerFragment
import com.pq.shiftmadeeasy.ui.pickers.TimePickerFragment
import com.pq.shiftmadeeasy.ui.viewmodels.RepositoryViewModel
import com.pq.shiftmadeeasy.ui.viewmodels.ViewModelProviderFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_todo_bottom_sheet.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddTodoBottomSheetFragment : BottomSheetDialogFragment(), AdapterView.OnItemSelectedListener  {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory
    private val repositoryViewModel: RepositoryViewModel by viewModels {
        viewModelProviderFactory
    }
    private val addTodoViewModel: AddTodoTaskViewModel by viewModels {
        viewModelProviderFactory
    }
    private var saveTaskListener: SaveTaskListener? = null
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    private lateinit var fromTimeSetListener: TimePickerDialog.OnTimeSetListener
    private lateinit var toTimeSetListener: TimePickerDialog.OnTimeSetListener

    private lateinit var binding: FragmentAddTodoBottomSheetBinding
    private var taskTimeInMillisecond: Long = 0
    private var spinnerItemPosition = 0

    interface SaveTaskListener {
        fun onTaskSaved()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setObservers()
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_todo_bottom_sheet,
            container,
            false
        )
        binding.apply {
            lifecycleOwner = this@AddTodoBottomSheetFragment
            viewModel = addTodoViewModel
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is SaveTaskListener){
            saveTaskListener = context as SaveTaskListener
        }else{
            throw RuntimeException("$context must implement SaveTaskListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Click Listeners
        setListeners()
        setUpSpinner()
        addTodoViewModel.setData()
        repositoryViewModel.reminderCalender
    }

    private fun setUpSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.position_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                taskDestinationSpinnerId.adapter = adapter
            }
            taskDestinationSpinnerId.onItemSelectedListener = this
        }
    }

    private fun setListeners() {
        saveButton.setOnClickListener {
            saveTask()
        }
        dateDisplayTextId?.setOnClickListener {
            context?.let {
                DatePickerFragment(dateSetListener).show(parentFragmentManager, DATE_PICKER)
            }
        }
        fromTimeDisplayId?.setOnClickListener {
            context?.let {
                TimePickerFragment(fromTimeSetListener, true).show(parentFragmentManager, FROM_TIME_PICKER )
            }
        }
        toTimeDisplayId?.setOnClickListener {
            context?.let {
                TimePickerFragment(toTimeSetListener, true).show(parentFragmentManager, TO_TIME_PICKER )
            }
        }
        setDateListener()
        setFromTimeListener()
    }

    private fun setObservers() {
        repositoryViewModel.reminderCalender.observe(viewLifecycleOwner, Observer {
            //textView.text = calendar.time.toString()
        })
        addTodoViewModel.taskTimStamp.observe(viewLifecycleOwner, Observer {
            taskTimeInMillisecond = it
        })
    }

    private fun setFromTimeListener() {
        var calender = Calendar.getInstance()
        fromTimeSetListener =
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                addTodoViewModel.setFromTime(hourOfDay, minute)
            }
        toTimeSetListener =
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                addTodoViewModel.setToTime(hourOfDay, minute)
            }
    }

    private fun setDateListener() {

        dateSetListener =
            DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
                //val formattedDate = sdf.format(calender)
                addTodoViewModel.setDate(year, monthOfYear, dayOfMonth)
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)

    private fun saveTask() {
        var task =
            Task(
                taskDate = dateDisplayTextId.text.toString(),
                taskFromTime = fromTimeDisplayId.text.toString(),
                taskToTime = toTimeDisplayId.text.toString(),
                position = taskDestinationSpinnerId.getItemAtPosition(spinnerItemPosition) as String?,
                taskLoggedHours = taskHoursDisplayId.text.toString(),
                taskTimeInMillisecond = taskTimeInMillisecond
            )
            repositoryViewModel.insert(task)
        saveTaskListener?.onTaskSaved()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        spinnerItemPosition = position
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    companion object {
        const val DATE_PICKER = "DATE_PICKER"
        const val FROM_TIME_PICKER = "FROM_TIME_PICKER"
        const val TO_TIME_PICKER = "TO_TIME_PICKER"
        fun newInstance() =
            AddTodoBottomSheetFragment().apply {
            }
    }
}