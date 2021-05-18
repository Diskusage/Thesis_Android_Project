package com.app.greenpass.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DaoPerson {

    @Query("SELECT * FROM people WHERE personCode = :code LIMIT 1")
    fun getPerson(code: Int): People

    @Query("SELECT * FROM people")
    fun getAllRecords(): List<People>

    @Query("DELETE FROM people WHERE personCode = :personModel")
    fun deletePerson(personModel: Int)

    @Query("SELECT CASE WHEN EXISTS(SELECT * FROM people WHERE :code = personCode) THEN 1 ELSE 0 END")
    fun checkIfExists(code: Int): Int

    @Insert
    fun addPerson(personModel: People)
}