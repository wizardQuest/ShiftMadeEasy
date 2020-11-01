package com.pq.focuslist.util.itemtouchhelper

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int):Boolean

    fun onItemSwipedToDelete(position: Int)

    fun onItemSwipedToComplete(position: Int)

    //for updating Database, will be called from clearView
    fun moveItemAfterMoveEventComplete()
}