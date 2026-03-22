package com.supersonic.evercard.features.root.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainFragmentViewModel : ViewModel() {

    private val _currentTab = MutableStateFlow<Int?>(null)
    val currentTab: StateFlow<Int?> = _currentTab.asStateFlow()

    fun setCurrentTab(position: Int) {
        _currentTab.value = position
    }
}