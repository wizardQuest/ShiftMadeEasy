package com.pq.shiftmadeeasy.ui.newuserlanding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pq.shiftmadeeasy.R
import com.pq.shiftmadeeasy.base.BaseFragment
import com.pq.shiftmadeeasy.base.hideToolbar
import com.pq.shiftmadeeasy.resources.Professions.Companion.getAllProfessions
import com.pq.shiftmadeeasy.resources.ProfessionsStructure
import com.pq.shiftmadeeasy.settings.SettingsManager
import com.pq.shiftmadeeasy.ui.dialogs.ProfessionSelectorDialogFragment
import com.pq.shiftmadeeasy.ui.dialogs.ProfessionSelectorDialogFragment.Companion.PROFESSION_SELECTED_DATA
import com.pq.shiftmadeeasy.ui.dialogs.ProfessionSelectorDialogFragment.Companion.PROFESSION_SELECTED_RESULT_CODE
import com.pq.shiftmadeeasy.ui.viewmodels.ViewModelProviderFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.new_user_landing_fragment.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NewUserLandingFragment : BaseFragment() {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory
    private val viewModel: NewUserLandingViewModel by viewModels {
        viewModelProviderFactory
    }
    private var userProfessionIcon: Int = -1
    var userProfessionName: String = ""

    @Inject
    lateinit var settingsManager: SettingsManager

    companion object {
        const val PROFESSION_SELECTION_REQUEST_CODE = 101
        fun newInstance() = NewUserLandingFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isBottomNavigationRequired = false
        activity?.hideToolbar()
        return inflater.inflate(R.layout.new_user_landing_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Click Listeners
        isFirstTimeUser()
        setListeners()
        setLiveData()
    }

    private fun isFirstTimeUser() {
        var isFirstTimeUser: Boolean = true
        settingsManager.userName.asLiveData().observeForever {
            if (!it.isNullOrBlank()) {
                isFirstTimeUser = false
                //findNavController().navigate(R.id.action_newUserLandingFragment_to_userHomeFragment)
            }
        }
    }

    private fun setListeners() {
        professionSelectorId?.setOnClickListener {
            val professionSelectorFragment = ProfessionSelectorDialogFragment.newInstance()
            professionSelectorFragment.setTargetFragment(this, PROFESSION_SELECTION_REQUEST_CODE)
            professionSelectorFragment.show(parentFragmentManager, "A")
        }
        getStartedButton?.setOnClickListener {
            //viewModel.getStarted(userNameTextFieldId.text.toString())
            getStarted()
        }
    }

    private fun setLiveData() {
        viewModel.userProfessionIcon.observe(viewLifecycleOwner, {
            userProfessionIcon = it
        })
        viewModel.userProfessionName.observe(viewLifecycleOwner, {
            userProfessionName = it
        })
    }

    private fun getStarted() {
        val userName = nameOutlinedTextFieldId?.editText?.text.toString()
        if (userProfessionName == "") {
            userProfessionName = getAllProfessions().first().professionName
        }
        if (userProfessionIcon == -1) {
            userProfessionIcon = getAllProfessions().first().professionIcon
        }
        lifecycleScope.launch {
            settingsManager.apply {
                setUserProfession(
                    userProfessionName = userProfessionName,
                    userProfessionIcon = userProfessionIcon
                )
            }
        }
        lifecycleScope.launch {
            settingsManager.apply {
                setUserName(userName)
            }
        }
        findNavController().navigate(R.id.action_newUserLandingFragment_to_userHomeFragment)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            PROFESSION_SELECTION_REQUEST_CODE -> {
                when (resultCode) {
                    PROFESSION_SELECTED_RESULT_CODE -> {
                        val profession: ProfessionsStructure? =
                            data?.getBundleExtra(PROFESSION_SELECTED_DATA)
                                ?.getParcelable(PROFESSION_SELECTED_DATA)
                        profession?.professionIcon?.let {
                            professionImageId?.setImageResource(it)
                            viewModel.setProfessionIcon(it)
                        }
                        profession?.professionName?.let {
                            professionNameId?.text = it
                            viewModel.setProfessionName(it)
                        }
                        //viewModel.setProfession()
                    }
                }
            }
        }
    }
}