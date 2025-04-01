package com.hi.zoo.data.local

import androidx.room.*
import com.hi.zoo.model.Pavilion

@Dao
abstract class PavilionDao {
    @Query("SELECT COUNT(id) FROM pavilion")
    abstract fun getSize(): Int

    @Query("SELECT * FROM pavilion")
    abstract fun getPavilions(): List<Pavilion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg pavilion: Pavilion): LongArray

    @Update
    abstract fun update(vararg pavilion: Pavilion): Int

    @Delete
    abstract fun delete(vararg pavilion: Pavilion): Int
}