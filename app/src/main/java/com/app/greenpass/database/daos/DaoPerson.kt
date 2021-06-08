package com.app.greenpass.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.greenpass.database.dataclasses.People

@Dao
interface DaoPerson {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePerson(person : People)

    @Query("SELECT * FROM people WHERE personCode = :code LIMIT 1")
    fun getPerson(code: Int): People?

}