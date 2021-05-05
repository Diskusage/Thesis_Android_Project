package com.example.sqliteapp.activities.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.sqliteapp.database.AppDatabase
import kotlinx.coroutines.*
import java.util.*
import kotlin.NoSuchElementException

class MainActivityViewModel(application: Application): AndroidViewModel(application) {

    @Throws(IllegalArgumentException::class, NoSuchElementException::class)
    fun onClickedLogin(name: String, sName: String, idnp: String, handler: CoroutineExceptionHandler){
        if (
                name.isEmpty() ||
                sName.isEmpty() ||
                idnp.isEmpty()
        ) {
            throw IllegalArgumentException()
        }
        runBlocking(handler) {
            val jobOne = GlobalScope.async(Dispatchers.Default) {
                AppDatabase.getInstance(getApplication()).DaoPerson().checkLogin(idnp)
            }
            val jobTwo = GlobalScope.async(Dispatchers.Default) {
                AppDatabase.getInstance(getApplication()).DaoTest().getLastTestForPerson(idnp) != null
            }
            if (jobOne.await() || jobTwo.await()) {
                return@runBlocking
            } else {
                throw NoSuchElementException()
            }
        }
        return
    }
}