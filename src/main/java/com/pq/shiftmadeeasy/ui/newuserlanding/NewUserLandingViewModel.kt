package com.pq.shiftmadeeasy.ui.newuserlanding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class NewUserLandingViewModel @Inject constructor() : ViewModel() {

    private var _userProfessionIcon = MutableLiveData<Int>()
    val userProfessionIcon: LiveData<Int>
        get() = _userProfessionIcon

    private var _userProfessionName = MutableLiveData<String>()
    val userProfessionName: LiveData<String>
        get() = _userProfessionName


    fun setProfessionIcon(resource: Int) {
        _userProfessionIcon.value = resource
    }

    fun setProfessionName(professionName: String){
        _userProfessionName.value = professionName
    }

}