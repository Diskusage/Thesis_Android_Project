package com.app.greenpass.activities.persondb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.greenpass.database.AppDatabase
import com.app.greenpass.database.toMap
import com.app.greenpass.models.PersonModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class DatabasePersonActivityViewModel(application: Application): AndroidViewModel(application)   {
    private val idnpLength = 13
    private var latestList: MutableLiveData<MutableList<PersonModel>> = MutableLiveData()
    val list: LiveData<MutableList<PersonModel>>
        get() = latestList

    fun initList(handler: CoroutineExceptionHandler) {
        viewModelScope.launch(handler) {
            val list = AppDatabase.getInstance(getApplication()).DaoPerson().getAllRecords()
            val forAd = ArrayList<PersonModel>()
            for (t in list){
                forAd.add(t.toMap())
            }
            latestList.value = forAd

        }
    }

    fun onAdd(handler: CoroutineExceptionHandler, model: PersonModel): Boolean{
        val ret = addRoutine(model)
        initList(handler)
        return ret
    }

    fun checkFields(name: String, sName: String, idnp: String): String?{
        if (idnp.isEmpty() || name.isEmpty() || sName.isEmpty()) {
            return "Fill in all the fields"
        }
        if (idnp.length != idnpLength) {
            return "IDNP length is 13 symbols"
        }
        return null
    }

    private fun addRoutine(personModel: PersonModel): Boolean{
        return try {
            AppDatabase.getInstance(getApplication()).DaoPerson().addPerson(personModel.toMap())
            true
        } catch (e: Exception){
            false
        }
    }
}