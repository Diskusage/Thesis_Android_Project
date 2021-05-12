package com.app.greenpass.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DaoTest {

    @Query("SELECT * FROM tests WHERE PERSON_CODE = :idnp ORDER BY TEST_DATE DESC LIMIT 1")
    fun getLastTestForPerson(idnp: Int): Tests?

    @Query("SELECT * FROM tests")
    fun getAllRecords(): List<Tests>

    @Query("SELECT * FROM tests WHERE PERSON_CODE = :idnp")
    fun getAllTestForPerson(idnp: Int): List<Tests>

    @Query("DELETE FROM tests WHERE id = :testModel")
    fun deleteTest(testModel: Int)

    @Query("DELETE FROM tests WHERE :code = PERSON_CODE")
    fun deleteAllByPerson(code: Int)

    @Query("SELECT id FROM tests WHERE :personModel = PERSON_CODE")
    fun getIdForDeletion(personModel: Int): List<Int>

    @Insert
    fun addTest(testModel: Tests)
}