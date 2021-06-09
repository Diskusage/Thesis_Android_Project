package com.app.greenpass.database.daos

import androidx.room.Dao
import androidx.room.Query
import com.app.greenpass.database.dataclasses.Vaccinations

@Dao
interface DaoVaccinations {
    @Query("SELECT * FROM vaccinations WHERE PERSON_CODE = :idnp")
    fun getRecordsForPerson(idnp: Int): List<Vaccinations>

    @Query("SELECT * FROM vaccinations WHERE PERSON_CODE = :indp ORDER BY VACCINATION_DATE LIMIT 1")
    fun getLastVaccination(indp: Int): Vaccinations?

}