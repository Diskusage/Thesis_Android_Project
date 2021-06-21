package com.app.greenpass.loginFragments.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.greenpass.database.dataclasses.People
import com.app.greenpass.database.dataclasses.Subject
import com.app.greenpass.database.dataclasses.Tests
import com.app.greenpass.database.dataclasses.Vaccinations
import com.app.greenpass.database.enums.Bases
import com.app.greenpass.database.factory.DatabaseFactory
import com.app.greenpass.database.firebase.FirebaseStorage
import com.app.greenpass.database.room.AppDatabase
import com.app.greenpass.models.PersonModel
import com.app.greenpass.util.CoroutineHelper
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineExceptionHandler

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    protected var person: PersonModel? = null
    protected val viewResult: MutableLiveData<State> = MutableLiveData()
    protected val fireStorage: FirebaseFirestore = FirebaseFirestore.getInstance()
    protected val helper = CoroutineHelper(application)
    protected val fireDb: FirebaseStorage =
        DatabaseFactory.getDatabase(Bases.Firebase, application) as FirebaseStorage
    protected val roomDb: AppDatabase =
        DatabaseFactory.getDatabase(Bases.Room, application) as AppDatabase
    val viewsResult: LiveData<State>
        get() = viewResult

    private suspend fun getFromCacheOrElse(subject: Subject): Subject?{
        return when (subject){
            is Vaccinations -> {
                roomDb.DaoTest().getLastTestForPerson(person.hashCode()) ?: let {
                    val toSave = fireDb.daoTest().getLastTestForPerson(person.hashCode())
                    toSave?.let { roomDb.DaoTest().saveTest(it) }
                    toSave
                }
            }
            is Tests -> {
                roomDb.DaoVaccinations().getLastVaccination(person.hashCode()) ?: let {
                    val toSave = fireDb.daoVaccinations().getLastVaccination(person.hashCode())
                    toSave?.let { roomDb.DaoVaccinations().saveVaccination(it) }
                    toSave
                }
            }
            is People -> {
                roomDb.DaoPerson().getPerson(person.hashCode()) ?: let {
                    val toSave = fireDb.daoPerson().getPerson(person.hashCode())
                    toSave?.let { roomDb.DaoPerson().savePerson(it) }
                    toSave
                }
            }
            else -> {
                null
            }
        }
    }
}