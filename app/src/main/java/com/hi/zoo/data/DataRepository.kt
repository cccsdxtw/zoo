package com.hi.zoo.data

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.hi.zoo.R
import com.hi.zoo.model.Pavilion
import com.hi.zoo.model.Plant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val application: Application,
    private val remoteDataSource: DataSource,
    private val localDataSource: DataSource
) {
    private suspend fun toastRemoteError() {
        withContext(Dispatchers.Main) {
            Toast.makeText(
                application,
                application.getString(R.string.request_failed_load_db),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    suspend fun loadPavilions(query: String = "", limit: Int = 0, offset: Int = 0): List<Pavilion> {
        val result = remoteDataSource.loadPavilions(query, limit, offset) // remote first
        Log.d("DataRepository", "Remote Result: isSuccess=${result.isSuccess}, dataSize=${result.data.size}")

        return if (result.isSuccess) {
            if (result.data.isNotEmpty()) {
                localDataSource.savePavilions(*result.data.toTypedArray()) // store to db
            }
            result.data
        } else { // if failed, get from local
            Log.e("DataRepository", "Remote data fetch failed, using local database")
            toastRemoteError()
            localDataSource.loadPavilions(query, limit, offset).data
        }
    }
    suspend fun loadPlants(
        query: String,
        limit: Int = 0,
        offset: Int = 0
    ): List<Plant> {
        Log.d("DataRepository", "Loading plants for query: $query, limit: $limit, offset: $offset")  // 輸出查詢參數
        val result = remoteDataSource.loadPlants(query, limit, offset)

        // 檢查載入結果
        if (result.isSuccess) {
            Log.d("DataRepository", "Remote data loaded successfully, size: ${result.data.size}")
            if (result.data.isNotEmpty()) {
                localDataSource.savePlants(*result.data.toTypedArray())
                Log.d("DataRepository", "Saved plants to local database")
            }
            return result.data
        } else {
            Log.e("DataRepository", "Error loading remote data: ${result.toString()}")  // 輸出錯誤資訊
            toastRemoteError()

            val localPlants = localDataSource.loadPlants(query, limit, offset).data
            Log.d("DataRepository", "Loaded plants from local database, size: ${localPlants.size}")
            return localPlants
        }
    }

}
