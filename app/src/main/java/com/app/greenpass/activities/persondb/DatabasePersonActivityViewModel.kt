package com.app.greenpass.activities.persondb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.greenpass.database.AppDatabase
import com.app.greenpass.database.toMap
import com.app.greenpass.models.PersonModel
import com.app.greenpass.util.CoroutineHelper
import kotlinx.coroutines.launch

class DatabasePersonActivityViewModel(application: Application): AndroidViewModel(application)   {
    private val idnpLength = 13
    private var latestList: MutableLiveData<MutableList<PersonModel>> = MutableLiveData()
    val list: LiveData<MutableList<PersonModel>>
        get() = latestList

    private val handler = CoroutineHelper(getApplication()).handler

    fun initList() {
        viewModelScope.launch(handler) {
            latestList.value = AppDatabase
                .getInstance(getApplication())
                .DaoPerson()
                .getAllRecords()
                .map { it.toMap() }
                .toMutableList()
        }
    }

    fun addPerson(name: String, sname: String, idnp: String): Pair<Boolean, String?> {
        return checkFields(name, sname, idnp)?.let { Pair(false, it) } ?: Pair(onAdd(PersonModel(name, sname, idnp)), null)
    }

    private fun onAdd(model: PersonModel): Boolean{
        val ret = addRoutine(model)
        initList()
        return ret
    }

    private fun checkFields(name: String, sName: String, idnp: String): String?{
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