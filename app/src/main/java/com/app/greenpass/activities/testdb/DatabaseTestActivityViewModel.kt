package com.app.greenpass.activities.testdb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.greenpass.database.AppDatabase
import com.app.greenpass.database.toMap
import com.app.greenpass.models.TestModel
import kotlinx.coroutines.*
import java.security.InvalidKeyException

class DatabaseTestActivityViewModel(application: Application): AndroidViewModel(application)  {
    private val idnpLength = 13
    private var latestList: MutableLiveData<MutableList<TestModel>> = MutableLiveData()
    val list: LiveData<MutableList<TestModel>>
        get() = latestList

    suspend fun initList(handler: CoroutineExceptionHandler) {
        val job = GlobalScope.async(handler) {
            val list = AppDatabase.getInstance(getApplication()).DaoTest().getAllRecords()
            val forAdapter = ArrayList<TestModel>()
            for (t in list){
                forAdapter.add(t.toMap())
            }
            forAdapter
        }
        latestList.value = job.await().toMutableList()
    }

    fun onAdd(handler: CoroutineExceptionHandler, model: TestModel){
        GlobalScope.launch (Dispatchers.Main + handler){
            addRoutine(model)
            initList(handler)
        }
    }

    fun checkFields(idnp: String, date: String){
        if (idnp.isEmpty() || date.isEmpty()) {
            throw IllegalArgumentException()
        }
        if (idnp.length != idnpLength) {
            throw InvalidKeyException()
        }
    }

    private fun addRoutine(personModel: TestModel){

        AppDatabase.getInstance(getApplication()).DaoTest().addTest(personModel.toMap())
    }
}