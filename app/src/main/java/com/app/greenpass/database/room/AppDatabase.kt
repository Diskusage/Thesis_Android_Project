package com.app.greenpass.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.greenpass.database.daos.DaoPerson
import com.app.greenpass.database.daos.DaoTest
import com.app.greenpass.database.daos.DaoVaccinations
import com.app.greenpass.database.dataclasses.People
import com.app.greenpass.database.dataclasses.Tests
import com.app.greenpass.database.dataclasses.Vaccinations
import com.app.greenpass.database.factory.IStorage

@Database(
    entities = [People::class, Tests::class, Vaccinations::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase(), IStorage {

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
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