package com.supersonic.evercard.features.root.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainFragmentViewModel : ViewModel() {
    private val _currentTab = MutableLiveData<Int>(0)
    val currentTab: LiveData<Int> get() = _currentTab
}