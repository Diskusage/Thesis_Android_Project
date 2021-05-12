package com.app.greenpass.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [People::class, Tests::class, Vaccinations::class], version = 1)
abstract class AppDatabase : RoomDatabase(){

    companion object {
       private var instance : AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            if (instance == null){
                instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "database"
                ).allowMainThreadQueries().build()
            }
            return instance as AppDatabase
        }
    }
    abstract fun DaoPerson(): DaoPerson
    abstract fun DaoVaccinations(): DaoVaccinations
    abstract fun DaoTest(): DaoTest
}