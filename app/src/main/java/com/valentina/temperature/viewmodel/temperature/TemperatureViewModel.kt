package com.valentina.temperature.viewmodel.temperature

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TemperatureViewModel:ViewModel() {
    private var _progressState = MutableStateFlow(0f)
    var progressState = _progressState.asStateFlow()

    fun setProgress(progress:Float){
        _progressState.value = progress
    }
}