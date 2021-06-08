package com.app.greenpass.database.daos

import androidx.room.Dao
import androidx.room.Query
import com.app.greenpass.database.dataclasses.People

@Dao
interface DaoPerson  {

    @Query("SELECT * FROM people WHERE personCode = :code LIMIT 1")
    fun getPerson(code: Int): People?

}