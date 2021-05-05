package com.example.sqliteapp.activities.vaccdb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sqliteapp.database.AppDatabase
import com.example.sqliteapp.database.toMap
import com.example.sqliteapp.models.PersonModel
import kotlinx.coroutines.*
import java.security.InvalidKeyException

class DatabaseActivityViewModel (application: Application): AndroidViewModel(application)  {
    private val idnpLength = 13
    private var latestList: MutableLiveData<MutableList<PersonModel>> = MutableLiveData()
    val list: LiveData<MutableList<PersonModel>>
        get() = latestList

    suspend fun initList(handler: CoroutineExceptionHandler) {
        val job = GlobalScope.async(handler) {
            val list = AppDatabase.getInstance(getApplication()).DaoPerson().getAllRecords()
            val forAdapter = ArrayList<PersonModel>()
            for (t in list){
                forAdapter.add(t.toMap())
            }
            forAdapter
        }
        latestList.value = job.await().toMutableList()
    }

    fun onAdd(handler: CoroutineExceptionHandler, model: PersonModel){
        GlobalScope.launch (Dispatchers.Main + handler){
            addRoutine(model)
            initList(handler)
        }
    }

    fun checkFields(fName: String, sName: String, idnp: String, vacc:String, date: String){
        if (
                idnp.isEmpty() ||
                date.isEmpty() ||
                fName.isEmpty() ||
                sName.isEmpty() ||
                vacc.isEmpty()
        ) {
            throw IllegalArgumentException()
        }
        if (idnp.length != idnpLength) {
            throw InvalidKeyException()
        }
    }

    private fun addRoutine(personModel: PersonModel){

        AppDatabase.getInstance(getApplication()).DaoPerson().addPerson(personModel.toMap())
    }
}