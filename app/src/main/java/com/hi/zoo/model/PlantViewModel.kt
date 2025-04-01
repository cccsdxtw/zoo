package com.hi.zoo.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hi.zoo.data.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {
    private val _plants = MutableStateFlow<List<Plant>>(emptyList())  // 用來儲存植物資料
    val plants: StateFlow<List<Plant>> = _plants  // 外部觀察用的 StateFlow

    // 載入植物資料
    fun loadPlants(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("PlantViewModel", "Loading plants for: $query")  // 確認是否進入該函數
            val data = repository.loadPlants(query)  // 請求資料
            _plants.value = data  // 更新植物資料
            Log.d("PlantViewModel", "Plants loaded: ${data.size}")  // 確認資料大小
        }
    }
}
