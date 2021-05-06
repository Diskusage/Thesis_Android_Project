package com.app.greenpass.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DaoTest {

    @Query("SELECT * FROM tests WHERE IDNP = :idnp ORDER BY TEST_DATE DESC LIMIT 1")
    fun getLastTestForPerson(idnp: String?): Tests?

    @Query("SELECT * FROM tests")
    fun getAllRecords(): MutableList<Tests>

    @Query("SELECT * FROM tests WHERE IDNP = :idnp")
    fun getAllTestForPerson(idnp: String?): MutableList<Tests>

    @Query("DELETE FROM tests WHERE testCode = :testModel")
    fun deleteTest(testModel: String)

    @Insert
    fun addTest(testModel: Tests)
}