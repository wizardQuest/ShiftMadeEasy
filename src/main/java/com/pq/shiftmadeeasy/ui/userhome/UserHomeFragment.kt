package com.pq.shiftmadeeasy.ui.userhome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.pq.shiftmadeeasy.R
import com.pq.shiftmadeeasy.adapter.tasks.TasksListAdapter
import com.pq.shiftmadeeasy.base.BaseFragment
import com.pq.shiftmadeeasy.base.showBottomAppBarFAB
import com.pq.shiftmadeeasy.localdatabase.task.Task
import com.pq.shiftmadeeasy.settings.SettingsManager
import com.pq.shiftmadeeasy.ui.viewmodels.RepositoryViewModel
import com.pq.shiftmadeeasy.ui.viewmodels.ViewModelProviderFactory
import com.pq.shiftmadeeasy.util.TopSpacingDecorator
import com.pq.shiftmadeeasy.util.getTaskListInOrder
import com.pq.shiftmadeeasy.util.itemtouchhelper.RecyclerViewItemTouchHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.user_home_fragment.*
import javax.inject.Inject

@AndroidEntryPoint
class UserHomeFragment : BaseFragment(), TasksListAdapter.Interaction {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory
    private val repositoryViewModel: RepositoryViewModel by viewModels {
        viewModelProviderFactory
    }

    @Inject
    lateinit var settingsManager: SettingsManager

    private lateinit var tasksListAdapter: TasksListAdapter

    private lateinit var viewModel: UserHomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.showBottomAppBarFAB()
        setUserCustomScreen()
        initRecyclerView()
        addDataList()
        setListeners()
    }

    private fun setListeners(){
        calendarIconId?.setOnClickListener {
            findNavController().navigate(R.id.action_userHomeFragment_to_calendarFragment)
        }
    }

    private fun setUserCustomScreen() {
        settingsManager.userName.asLiveData().observe(viewLifecycleOwner, {
            userGreetingId.text = "Hi! $it sup"
        })
        settingsManager.userProfessionIconFlow.asLiveData().observe(viewLifecycleOwner, {
            userImageId.setImageResource(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserHomeViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun addDataList() {
        repositoryViewModel.getAllTasks().observe(viewLifecycleOwner, Observer { list ->
            tasksListAdapter.submitList(getTaskListInOrder(list))
        })
    }

    private fun initRecyclerView() {
        taskRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            val topSpacingDecorator = TopSpacingDecorator(30)
            addItemDecoration(topSpacingDecorator)
            tasksListAdapter =
                TasksListAdapter(this@UserHomeFragment)

            val callback = RecyclerViewItemTouchHelper(tasksListAdapter)
            val itemTouchHelper = ItemTouchHelper(callback)
            tasksListAdapter.setTouchHelper(itemTouchHelper)
            itemTouchHelper.attachToRecyclerView(taskRecyclerView)
            adapter = tasksListAdapter
        }
    }

    companion object {
        fun newInstance() = UserHomeFragment()
    }

    override fun onItemSelected(position: Int) {
        //findNavController().navigate(R.id.action_userHomeFragment_to_taskHomeFragment)
    }

    override fun onItemDeleted(task: Task) {
        repositoryViewModel.delete(task)
    }
}