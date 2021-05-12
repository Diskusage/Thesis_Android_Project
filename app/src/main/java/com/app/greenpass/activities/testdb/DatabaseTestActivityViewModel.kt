package com.app.greenpass.activities.testdb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.greenpass.database.AppDatabase
import com.app.greenpass.database.toMap
import com.app.greenpass.models.TestModel
import kotlinx.coroutines.*

class DatabaseTestActivityViewModel(application: Application): AndroidViewModel(application)  {
    private var latestList: MutableLiveData<MutableList<TestModel>> = MutableLiveData()
    val list: LiveData<MutableList<TestModel>>
        get() = latestList

    fun initList(handler: CoroutineExceptionHandler) {
        viewModelScope.launch(handler) {
            val list = AppDatabase.getInstance(getApplication()).DaoTest().getAllRecords()
            val forAdapter = ArrayList<TestModel>()
            for (t in list){
                forAdapter.add(t.toMap())
            }
            latestList.value = forAdapter.toMutableList()
        }
    }

    fun onAdd(handler: CoroutineExceptionHandler, model: TestModel): Boolean{
        val ret = addRoutine(model)
        initList(handler)
        return ret
    }

    fun checkFields(idnp: String, date: String): String?{
        if (idnp.isEmpty() || date.isEmpty()) {
            return "Fill in all the fields"
        }
        return null
    }

    private fun addRoutine(personModel: TestModel): Boolean {
        return try {
            AppDatabase.getInstance(getApplication()).DaoTest().addTest(personModel.toMap())
            true
        } catch (e: Exception){
            false
        }
    }
}