//package com.app.greenpass.activities.testdb
//
//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.viewModelScope
//import com.app.greenpass.database.room.AppDatabase
//import com.app.greenpass.database.toMap
//import com.app.greenpass.models.TestModel
//import kotlinx.coroutines.*
//
//class DatabaseTestActivityViewModel(application: Application): AndroidViewModel(application)  {
//    private var latestList: MutableLiveData<MutableList<TestModel>> = MutableLiveData()
//    val list: LiveData<MutableList<TestModel>>
//        get() = latestList
//
//    fun initList(handler: CoroutineExceptionHandler) {
//        viewModelScope.launch(handler) {
//            latestList.value = AppDatabase
//                .getInstance(getApplication())
//                .DaoTest()
//                .getAllRecords()
//                .map { it.toMap() }
//                .toMutableList()
//        }
//    }
//
//    fun onAdd(handler: CoroutineExceptionHandler, model: TestModel): Boolean{
//        val ret = addRoutine(model)
//        initList(handler)
//        return ret
//    }
//
//    fun checkFields(idnp: String, date: String): String?{
//        if (idnp.isEmpty() || date.isEmpty()) {
//            return "Fill in all the fields"
//        }
//        if (AppDatabase.getInstance(getApplication()).DaoPerson().checkIfExists(idnp.toInt()) == 0){
//            return "There is no such person"
//        }
//        return null
//    }
//
//    private fun addRoutine(personModel: TestModel): Boolean {
//        return try {
//            AppDatabase.getInstance(getApplication()).DaoTest().addTest(personModel.toMap())
//            true
//        } catch (e: Exception){
//            false
//        }
//    }
//}