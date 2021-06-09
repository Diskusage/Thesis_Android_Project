package com.app.greenpass.database.daos

import androidx.room.Dao
import androidx.room.Query
import com.app.greenpass.database.dataclasses.Tests

@Dao
interface DaoTest {

    @Query("SELECT * FROM tests WHERE PERSON_CODE = :idnp ORDER BY TEST_DATE DESC LIMIT 1")
    fun getLastTestForPerson(idnp: Int): Tests?

    @Query("SELECT * FROM tests WHERE PERSON_CODE = :idnp")
    fun getAllTestForPerson(idnp: Int): List<Tests>

}