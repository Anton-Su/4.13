package com.example.a413

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CurrencyState(
    val currentRate: Double = 90.5,
    val previousRate: Double = 90.5
)

class ViewModelHot: ViewModel()  {
    private val _rate = MutableStateFlow<CurrencyState>(CurrencyState(79.5, 79.5))
    val rate: StateFlow<CurrencyState> = _rate.asStateFlow()
    private val _timedate = MutableStateFlow<String>("2024-06-01")
    val timedate: StateFlow<String> = _timedate.asStateFlow()
    init {
        // Имитируем обновление данных
        simulateDataUpdates()
    }
    fun refreshRate() {
        generateNewRate()
    }

    private fun generateNewRate() {
        viewModelScope.launch {
            _rate.value = CurrencyState(
                Math.random() * 100,
                rate.value.currentRate
            ) // Генерируем случайный курс
            _timedate.value = "${System.currentTimeMillis() / 1000 % 60}" // Обновляем время
        }
    }


    private fun simulateDataUpdates() {
        viewModelScope.launch {
            while (true) {
                delay(5000)
                generateNewRate()
            }
        }
    }
}