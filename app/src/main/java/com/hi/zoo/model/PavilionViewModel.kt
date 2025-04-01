package com.hi.zoo.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hi.zoo.data.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PavilionViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    private val _pavilions = MutableStateFlow<List<Pavilion>>(emptyList())
    val pavilions: StateFlow<List<Pavilion>> = _pavilions.asStateFlow()

    init {
        loadPavilions()  // 🔥 App 啟動時就載入資料
    }

    fun loadPavilions(query: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.loadPavilions(query)
            _pavilions.value = data
        }
    }
}
