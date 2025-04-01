package com.hi.zoo.data.remote

import android.util.Log
import com.hi.zoo.data.DataSource
import com.hi.zoo.model.Pavilion
import com.hi.zoo.model.Plant
import timber.log.Timber

class RemoteDataSource(private val apiService: ApiService) : DataSource {

    override fun loadPavilions(
        query: String,
        limit: Int,
        offset: Int
    ): DataSource.Result<List<Pavilion>> {
        Log.d("RemoteDataSource", "Requesting Pavilions: query=$query, limit=$limit, offset=$offset")

        return try {
            val response = apiService.getPavilion(query, limit, offset).apply {
                // 在執行 API 請求之前，打印 URL
                Log.d("ApiService", "Request URL: ${this.request().url}")
            }.execute()
            // 記錄響應狀態
            Log.d("RemoteDataSource", "Response status: ${response.code()}")

            if (response.isSuccessful) {
                val pavilions = response.body()?.result?.results
                Log.d("RemoteDataSource", "Received Pavilions: ${pavilions?.size ?: 0}")
                DataSource.Result(pavilions ?: listOf(), true)
            } else {
                Log.e("RemoteDataSource", "API call failed: ${response.errorBody()?.string()}")
                DataSource.Result(listOf(), false)
            }

        } catch (e: Exception) {
            // 捕捉錯誤並記錄
            Log.e("RemoteDataSource", "Error fetching pavilions: ${e.message}", e)
            Timber.e(e)
            DataSource.Result(listOf(), false)
        }
    }

    override fun loadPlants(
        query: String,
        limit: Int,
        offset: Int
    ): DataSource.Result<List<Plant>> {
        Log.d("RemoteDataSource", "Requesting Plants: query=$query, limit=$limit, offset=$offset")

        return try {
            val response = apiService.getPlant(query, limit, offset).execute()

            // 記錄響應狀態
            Log.d("RemoteDataSource", "Response status: ${response.code()}")

            if (response.isSuccessful) {
                val plants = response.body()?.result?.results
                Log.d("RemoteDataSource", "Received Plants: ${plants?.size ?: 0}")
                DataSource.Result(plants ?: listOf(), true)
            } else {
                Log.e("RemoteDataSource", "API call failed: ${response.errorBody()?.string()}")
                DataSource.Result(listOf(), false)
            }
        } catch (e: Exception) {
            // 捕捉錯誤並記錄
            Log.e("RemoteDataSource", "Error fetching plants: ${e.message}", e)
            Timber.e(e)
            DataSource.Result(listOf(), false)
        }
    }
}
