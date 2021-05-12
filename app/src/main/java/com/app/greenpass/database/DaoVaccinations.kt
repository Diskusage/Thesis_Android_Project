package com.app.greenpass.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DaoVaccinations {
    @Query("SELECT * FROM vaccinations WHERE PERSON_CODE = :idnp")
    fun getRecordsForPerson(idnp: Int): List<Vaccinations>

    @Query("SELECT * FROM vaccinations")
    fun getAllRecords(): List<Vaccinations>

    @Query("SELECT * FROM vaccinations WHERE PERSON_CODE = :indp ORDER BY VACCINATION_DATE LIMIT 1")
    fun getLastVaccination(indp: Int): Vaccinations?

    @Query("DELETE FROM vaccinations WHERE id = :personModel")
    fun deletePerson(personModel: Int)

    @Query("DELETE FROM Vaccinations WHERE PERSON_CODE = :code")
    fun deleteFromPerson(code: Int)

    @Query("SELECT id FROM vaccinations WHERE :personModel = PERSON_CODE")
    fun getIdForDeletion(personModel: Int): List<Int>

    @Insert
    fun addEntry(personModel: Vaccinations)

}