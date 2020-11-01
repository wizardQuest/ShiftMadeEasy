package com.pq.shiftmadeeasy.base

import android.app.Activity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbarLayout
import kotlinx.android.synthetic.main.toolbar.*

fun Activity?.showBottomAppBarFAB() {
    this?.apply {
        bottomAppBarFAB.visibility = View.VISIBLE
    }
}

fun Activity?.hideBottomAppBarFAB() {
    this?.apply {
        bottomAppBarFAB.visibility = View.GONE
    }
}

fun Activity?.hideToolbar() {
    this.apply {
        this?.toolbarLayout?.visibility = View.GONE
    }
}

fun Activity?.showToolbar(toolbarTitle: String) {
    this.apply {
        this?.toolbarLayout?.apply {
            visibility = View.VISIBLE
            toolbarTitleId.text = toolbarTitle
        }
    }
}

fun Activity?.showToolbarAndHideBottomBar(toolbarTitle: String) {
    this.apply {
        hideBottomAppBarFAB()
        showToolbar(toolbarTitle)
    }
}