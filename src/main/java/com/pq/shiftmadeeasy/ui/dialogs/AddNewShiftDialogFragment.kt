package com.pq.shiftmadeeasy.ui.dialogs

import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.pq.focuslist.base.hideKeyboard
import com.pq.shiftmadeeasy.R
import com.pq.shiftmadeeasy.adapter.color.ColorListAdapter
import com.pq.shiftmadeeasy.color.Colors.Companion.getColors
import com.pq.shiftmadeeasy.localdatabase.shift.Shift
import com.pq.shiftmadeeasy.ui.calendarview.ShiftRepositoryViewModel
import com.pq.shiftmadeeasy.ui.viewmodels.ViewModelProviderFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.add_new_shift_dialog.*
import javax.inject.Inject

@AndroidEntryPoint
class AddNewShiftDialogFragment : DialogFragment(), ColorListAdapter.Interaction {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory
    private val shiftRepositoryViewModel: ShiftRepositoryViewModel by viewModels {
        viewModelProviderFactory
    }

    private lateinit var colorListAdapter: ColorListAdapter
    private var lastPosition: Int = NO_COLOR_SELECTED

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_new_shift_dialog, container, false)
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
        shiftRepositoryViewModel.getAllShifts().observe(viewLifecycleOwner, Observer { it ->

        })
        setColorListAdapter()
        setOnClickListeners()
    }

    private fun setColorListAdapter() {
        val colorList = getColors()
        colorListAdapter = ColorListAdapter((this@AddNewShiftDialogFragment))
        colorListAdapter.submitList(colorList)
        colorListViewId.apply {
            layoutManager = GridLayoutManager(context, 5, GridLayoutManager.VERTICAL, false)
            adapter = colorListAdapter
        }
    }

    private fun setOnClickListeners() {
        addButtonId?.setOnClickListener {
            actionOnAddButtonClick()
        }
        cancelButtonId?.setOnClickListener {
            targetFragment?.onActivityResult(
                targetRequestCode,
                CANCEL_ADD_SHIFT_RESULT_CODE,
                null
            )
            hideKeyboard()
            dialog?.dismiss()
        }
    }

    private fun actionOnAddButtonClick() {
        val shift = Shift()
        shift?.apply {
            if (lastPosition != NO_COLOR_SELECTED) {
                shiftColor = getColors()[lastPosition]
            }
            shiftTitle = addNewShiftTitle?.text?.toString() ?: ""
        }
        shiftRepositoryViewModel.apply {
            insert(shift = shift)
        }
        targetFragment?.onActivityResult(targetRequestCode, ADD_SHIFT_RESULT_CODE, null)
        hideKeyboard()
        dialog?.dismiss()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onItemSelected(position: Int, item: Int) {
        //Different color selected as last
        if (position != lastPosition) {
            setSelectedColor(position)
            if (lastPosition != NO_COLOR_SELECTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    colorListViewId[lastPosition].foreground = null
                } else {
                    @ColorInt val colorForLastGrid =
                        getCurrentThemeColor(R.attr.bottomSheetBackgroundColor)
                    colorListViewId[lastPosition].setBackgroundColor(colorForLastGrid)
                }
                //setSelectedColor(lastPosition)
            }
            lastPosition = position
        }
        //Same Color Selected as last
        else if (lastPosition == position) {
            lastPosition = NO_COLOR_SELECTED
            setSelectedColor(position)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setSelectedColor(position: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            colorListViewId[position].foreground =
                resources.getDrawable(R.drawable.ic_baseline_tick)
            colorListViewId[position].foreground.minimumHeight
        } else {
            @ColorInt val color = getCurrentThemeColor(R.attr.themeSecondaryComplimentaryColor)
            colorListViewId[position].setBackgroundColor(color)
        }
    }

    private fun getCurrentThemeColor(attr: Int): Int {
        val typedValue = TypedValue()
        val theme = activity?.theme
        theme?.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }

    companion object {
        const val NO_COLOR_SELECTED = -1
        const val ADD_SHIFT_RESULT_CODE = 101
        const val CANCEL_ADD_SHIFT_RESULT_CODE = 102
        fun newInstance(): AddNewShiftDialogFragment {
            val args = Bundle()
            val fragment =
                AddNewShiftDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}