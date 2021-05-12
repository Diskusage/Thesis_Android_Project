package com.app.greenpass.activities.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.app.greenpass.database.AppDatabase
import com.app.greenpass.database.toMap
import com.app.greenpass.models.PersonModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async

class MainActivityViewModel(application: Application): AndroidViewModel(application) {

    suspend fun onClickedLogin(name: String, sName: String, idnp: String, handler: CoroutineExceptionHandler)
    : PersonModel?{
        if (
                name.isEmpty() ||
                sName.isEmpty() ||
                idnp.isEmpty()
        ) {
            return null
        }
        return try {
            val job = viewModelScope.async(handler) {
                AppDatabase
                        .getInstance(getApplication())
                        .DaoPerson()
                        .getPerson(PersonModel(name, sName, idnp).hashCode())
                        .toMap()
            }
            job.await()
        } catch (e: Exception){
            null
        }
    }
}