package com.example.sqliteapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DaoPerson {
    @Query("SELECT * FROM personvaccinations WHERE PERSON_IDNP = :idnp ORDER BY PERSON_DATE DESC LIMIT 1")
    fun getLastRecordForPerson(idnp: String?): PersonVaccinations?

    @Query("SELECT * FROM personvaccinations WHERE PERSON_IDNP = :idnp")
    fun getRecordsForPerson(idnp: String?): MutableList<PersonVaccinations>

    @Query("SELECT * FROM personvaccinations")
    fun getAllRecords(): MutableList<PersonVaccinations>

    @Query("SELECT CASE WHEN EXISTS (SELECT * FROM personvaccinations WHERE PERSON_IDNP = :idnp) THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT) END")
    fun checkLogin(idnp: String?): Boolean

    @Query("DELETE FROM personvaccinations WHERE personalCode = :personModel")
    fun deletePerson(personModel: String)

    @Insert
    fun addPerson(personModel: PersonVaccinations)



}