package com.pq.shiftmadeeasy.ui.dialogs

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pq.focuslist.base.gone
import com.pq.focuslist.base.visible
import com.pq.shiftmadeeasy.R
import com.pq.shiftmadeeasy.adapter.professionselector.ProfessionSelectorAdapter
import com.pq.shiftmadeeasy.resources.Professions.Companion.getAllProfessions
import com.pq.shiftmadeeasy.resources.ProfessionsStructure
import kotlinx.android.synthetic.main.item_profession.view.*
import kotlinx.android.synthetic.main.profession_selector_dialog.*

class ProfessionSelectorDialogFragment : DialogFragment(), ProfessionSelectorAdapter.Interaction {

    private lateinit var professionSelectorAdapter: ProfessionSelectorAdapter
    private lateinit var selectedProfession: ProfessionsStructure
    private var lastPosition: Int = NO_PROFESSION_SELECTED


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profession_selector_dialog, container, false)
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
        setProfessionListAdapter()
        setListeners()
    }

    private fun setListeners() {
        setProfessionButtonId?.setOnClickListener {
            setProfessionOfUser()
        }
    }

    private fun setProfessionOfUser() {
        val data = Intent()
        val bundle = Bundle()
        if (lastPosition != NO_PROFESSION_SELECTED) {
            bundle.putParcelable(PROFESSION_SELECTED_DATA, getAllProfessions()[lastPosition])
        } else {
            bundle.putParcelable(PROFESSION_SELECTED_DATA, getAllProfessions().first())
        }
        data.putExtra(PROFESSION_SELECTED_DATA, bundle)
        targetFragment?.onActivityResult(targetRequestCode, PROFESSION_SELECTED_RESULT_CODE, data)
        dialog?.dismiss()
    }

    private fun setProfessionListAdapter() {
        professionSelectorAdapter = ProfessionSelectorAdapter(this@ProfessionSelectorDialogFragment)
        professionSelectorAdapter.submitList(getAllProfessions())
        selectedProfession = getAllProfessions().first()
        professionRecyclerViewId?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = professionSelectorAdapter
        }
    }

    override fun onItemSelected(position: Int) {
        selectedProfession = getAllProfessions()[position]
        if (position != lastPosition) {
            professionRecyclerViewId[position].isSelectedIconId.visible()
            if (lastPosition != NO_PROFESSION_SELECTED) {
                professionRecyclerViewId[lastPosition].isSelectedIconId.gone()
            }
            lastPosition = position
        }
        //Same Color Selected as last
        else if (lastPosition == position) {
            professionRecyclerViewId[lastPosition].isSelectedIconId.gone()
            lastPosition = NO_PROFESSION_SELECTED
        }
    }

    private fun getCurrentThemeColor(attr: Int): Int {
        val typedValue = TypedValue()
        val theme = activity?.theme
        theme?.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }


    companion object {
        const val NO_PROFESSION_SELECTED = -1
        const val PROFESSION_SELECTED_RESULT_CODE = 101
        const val PROFESSION_SELECTED_DATA = "PROFESSION_SELECTED_DATA"
        fun newInstance(): ProfessionSelectorDialogFragment {
            var args = Bundle()
            val fragment = ProfessionSelectorDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }


}