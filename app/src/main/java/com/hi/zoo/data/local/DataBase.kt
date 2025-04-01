package com.hi.zoo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hi.zoo.model.Pavilion
import com.hi.zoo.model.Plant


@Database(
    entities = [Pavilion::class, Plant::class],
    version = 1,
    exportSchema = false
)
abstract class DataBase : RoomDatabase() {

    abstract fun pavilionDao(): PavilionDao
    abstract fun plantDao(): PlantDao

    companion object {
        private const val NAME = "taipei_zoo.db"

        fun build(context: Context): DataBase {
            return Room.databaseBuilder(context, DataBase::class.java, NAME)
                .allowMainThreadQueries()
                .build()
        }
    }
}