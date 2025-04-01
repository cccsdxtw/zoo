package com.hi.zoo.data

import com.hi.zoo.model.Pavilion
import com.hi.zoo.model.Plant


interface DataSource {

    data class Result<T>(
        val data: T,
        val isSuccess: Boolean = true,
    )

    fun loadPavilions(
        query: String = "",
        limit: Int = 0,
        offset: Int = 0
    ): Result<List<Pavilion>>

    fun loadPlants(
        query: String = "",
        limit: Int = 0,
        offset: Int = 0
    ): Result<List<Plant>>

    fun savePavilions(vararg pavilion: Pavilion): LongArray {
        return longArrayOf()
    }

    fun savePlants(vararg plant: Plant): LongArray {
        return longArrayOf()
    }
}