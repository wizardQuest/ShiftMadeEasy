package com.pq.shiftmadeeasy.ui.dialogs

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pq.shiftmadeeasy.R
import com.pq.shiftmadeeasy.adapter.shifts.ShiftListAdapter
import com.pq.shiftmadeeasy.localdatabase.shift.Shift
import com.pq.shiftmadeeasy.ui.calendarview.ShiftRepositoryViewModel
import com.pq.shiftmadeeasy.ui.viewmodels.ViewModelProviderFactory
import com.pq.shiftmadeeasy.util.TopSpacingDecorator
import com.pq.shiftmadeeasy.util.getShiftListInOrder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.calendar_fragment.*
import javax.inject.Inject

@AndroidEntryPoint
class ShiftSelectorDialogFragment : DialogFragment(), ShiftListAdapter.Interaction {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory
    private val shiftRepositoryViewModel: ShiftRepositoryViewModel by viewModels {
        viewModelProviderFactory
    }
    private lateinit var shiftListAdapter: ShiftListAdapter
    private lateinit var shiftList: List<Shift>
    private lateinit var selectedDate: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.shift_selector_dialog_fragment, container, false)
        //making background transparent for perfect round corners
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return view
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setShiftListAdapter()
        selectedDate = arguments?.getString(GET_CALENDAR_DATE) ?: ""
        shiftRepositoryViewModel.getAllShifts().observe(viewLifecycleOwner, Observer { list ->
            shiftList = getShiftListInOrder(list)
            shiftListAdapter.submitList(shiftList)
        })
    }

    private fun setShiftListAdapter() {
        shiftRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            val topSpacingDecorator = TopSpacingDecorator(30)
            addItemDecoration(topSpacingDecorator)
            shiftListAdapter = ShiftListAdapter(this@ShiftSelectorDialogFragment)
            adapter = shiftListAdapter
        }
    }

    companion object {
        const val NO_COLOR_SELECTED = -1
        private const val GET_CALENDAR_DATE = "GET_CALENDAR_DATE"
        const val DAY_SELECTED_RESULT_CODE = 301
        const val CANCEL_ADD_SHIFT_RESULT_CODE = 302
        const val SHIFT_SELECTED_DATA = "SHIT_SELECTED_DATA"
        const val SHIFT_SELECTED_DATE = "SHIT_SELECTED_DATE"
        fun newInstance(calendarDate: String): ShiftSelectorDialogFragment {
            val args = Bundle()
            args.putString(GET_CALENDAR_DATE, calendarDate)
            val fragment =
                ShiftSelectorDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onItemSelected(position: Int) {
        val data = Intent()
        val bundle = Bundle()
        bundle.apply {
            putParcelable(SHIFT_SELECTED_DATA, shiftList[position])
            putString(SHIFT_SELECTED_DATE, selectedDate)
        }
        data.putExtra(SHIFT_SELECTED_DATA, bundle)
        targetFragment?.onActivityResult(
            targetRequestCode,
            DAY_SELECTED_RESULT_CODE, data
        )
        dialog?.dismiss()
    }
}