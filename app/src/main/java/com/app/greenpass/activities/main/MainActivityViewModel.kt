package com.app.greenpass.activities.main

import android.app.Application
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.app.greenpass.database.dataclasses.toMap
import com.app.greenpass.enums.Vaccines
import com.app.greenpass.loginFragments.viewmodels.BaseViewModel
import com.app.greenpass.loginFragments.viewmodels.State
import com.app.greenpass.models.PersonModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.random.Random

class MainActivityViewModel(application: Application): BaseViewModel(application) {
//    suspend fun onClickedLogin(userData: List<String>, handler: CoroutineExceptionHandler)
//    : PersonModel?{
//        if (userData.any { it.isEmpty() }){
//            return null
//        }
//        return try {
//            val job = viewModelScope.async(handler) {
////                AppDatabase
////                    .getInstance(getApplication())
////                    .DaoPerson()
////                    .getPerson(PersonModel(userData[0], userData[1], userData[2]).hashCode())
////                    .toMap()
//                DatabaseFactory
//                    .getDatabase(Bases.Firebase, getApplication())
//                    .daoPerson()
//                    .getPerson(PersonModel(userData[0], userData[1], userData[2])
//                    .hashCode())
//                    ?.toMap()
//            }
//            job.await()
//        } catch (e: Exception){
//            null
//        }
//    }



//    private fun dateGenerator() : String{
//        val day = Random.nextInt(1, 32).toString()
//        val month = Random.nextInt(1, 13).toString()
//        val year = Random.nextInt(2019, 2022).toString()
//        return "$day/$month/$year"
//    }
//
//    private fun getRandomVaccine() : Vaccines = Vaccines.fromInt(Random.nextInt(1, 4))
//
//    private fun getRandomLab() : String{
//        val labNames = arrayListOf(
//            "Science City",
//                    "Stream Diagnostix",
//                    "Superior Science",
//                    "Versatile Cool",
//                    "My Chem Lab",
//                    "TrueTrust lab",
//                    "Sci-Land",
//                    "Equitas lab",
//                    "Science Kings",
//                    "AdvantEDGE Diagnostic",
//                    "Spot Cool",
//                    "Exodoscience Dev"
//        )
//        return labNames[Random.nextInt(0, labNames.size)]
//    }
//
//    private fun getRandomSeries() : String{
//        val seriesNames = arrayListOf(
//            "Cov001",
//            "Cov002",
//            "Cov003",
//            "Cov004",
//            "Cov005"
//        )
//        return seriesNames[Random.nextInt(0, seriesNames.size)]
//    }

//    fun populateVaccinations(){
//        fireStorage.collection("users").get().addOnSuccessListener { result ->
//            for (document in result){
//                for (i in 1..2){
//                    val type = getRandomVaccine().toString()
//                    val vaccDate = dateGenerator()
//                    val lab = getRandomLab()
//                    val series = getRandomSeries()
//                    val owner = document["code"].toString().toInt()
//                    val map = hashMapOf(
//                        "date" to vaccDate,
//                        "type" to type,
//                        "laboratory" to lab,
//                        "series" to series,
//                        "owner" to owner
//                    )
//                    fireStorage
//                        .collection("vaccinations")
//                        .document()
//                        .set(map)
//                        .addOnFailureListener { Log.i(TAG, "Failed to add $map") }
//                        .addOnSuccessListener { Log.i(TAG, "Added $map") }
//                }
//            }
//        }
//    }
//
//    fun populateTests(){
//        fireStorage.collection("users").get().addOnSuccessListener { result ->
//            for (document in result){
//                for (i in 1..2){
//                    val testResult = Random.nextBoolean()
//                    val testDate = dateGenerator()
//                    val antibodies = testResult.not()
//                    val owner = document["code"].toString().toInt()
//                    val map = hashMapOf(
//                        "result" to testResult,
//                        "date" to testDate,
//                        "antibodies" to antibodies,
//                        "owner" to owner
//                    )
//                    fireStorage
//                        .collection("tests")
//                        .document()
//                        .set(map)
//                        .addOnFailureListener { Log.i(TAG, "Failed to add $map") }
//                        .addOnSuccessListener { Log.i(TAG, "Added $map") }
//                }
//            }
//        }
//
//    }
//
//    fun populateUsers(){
//        val fNames = arrayListOf(
//                    "Charlie",
//                    "Kennedy",
//                    "Tayla",
//                    "Lorena",
//                    "Ollie",
//                    "Kaisha",
//                    "Blake",
//                    "Paris",
//                    "Nevaeh",
//                    "Robbie"
//        )
//        val sNames = arrayListOf(
//                    "Fisher",
//                    "Olsen",
//                    "Stanton",
//                    "Atkins",
//                    "Kendall",
//                    "Donnelly",
//                    "Broadhurst",
//                    "Sykes",
//                    "Hume",
//                    "Clegg"
//        )
//        val idnpPool = HashSet<Long>()
//        for (i in 1..15){
//            var flag = true
//            val fName = fNames[Random.nextInt(fNames.size)]
//            val sName = sNames[Random.nextInt(fNames.size)]
//            var idnp: Long = 0
//            while (flag){
//                idnp = Random.nextLong(1000000000000, 10000000000000)
//                if (idnpPool.contains(idnp)){
//                    continue
//                } else {
//                    flag = false
//                    idnpPool.add(idnp)
//                }
//            }
//            val code = (fName + sName + idnp).hashCode().absoluteValue
//
//            val map = hashMapOf("name" to fName, "lastname" to sName, "code" to code, "idnp" to idnp)
//            fireStorage
//                .collection("users")
//                .document()
//                .set(map)
//                .addOnFailureListener { Log.i(TAG, "Failed to add $map") }
//                .addOnSuccessListener { Log.i(TAG, "Added $map") }
//        }
//    }
//
    fun getPerson(code: Int) {
    viewModelScope.launch(Dispatchers.IO) {
        val data = fireDb.daoPerson().getPerson(code)?.toMap()
        viewResult.postValue(ViewResult.Downloaded(data))
    }
}

    sealed class ViewResult : State {
        class Downloaded(
            val person: PersonModel?,
        ) : ViewResult()
    }
}