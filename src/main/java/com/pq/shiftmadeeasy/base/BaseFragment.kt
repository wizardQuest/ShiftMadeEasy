package com.pq.shiftmadeeasy.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    var isBottomNavigationRequired = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (isBottomNavigationRequired) {
            true -> {
                activity?.apply {
                    showBottomAppBarFAB()
                    hideToolbar()
                }
            }
            false -> {
                activity?.hideBottomAppBarFAB()
            }
        }
    }

   /* private fun hideToolbar() {
        (activity as? AppCompatActivity)?.apply {
            supportActionBar?.hide()
        }
    }

    private fun showToolbar() {
        var toolbarTitle = "Settings"
        (activity as? AppCompatActivity)?.apply {
            supportActionBar?.apply {
                title = toolbarTitle
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }
            title = toolbarTitle
        }
    }*/
}