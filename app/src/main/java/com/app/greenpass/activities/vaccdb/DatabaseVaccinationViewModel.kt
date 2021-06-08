//package com.app.greenpass.activities.vaccdb
//
//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.viewModelScope
//import com.app.greenpass.database.room.AppDatabase
//import com.app.greenpass.database.toMap
//import com.app.greenpass.enums.Vaccines
//import com.app.greenpass.models.VaccinationModel
//import kotlinx.coroutines.CoroutineExceptionHandler
//import kotlinx.coroutines.launch
//
//class DatabaseVaccinationViewModel (application: Application): AndroidViewModel(application)  {
//    private var latestList: MutableLiveData<MutableList<VaccinationModel>> = MutableLiveData()
//    val list: LiveData<MutableList<VaccinationModel>>
//        get() = latestList
//
//    fun initList(handler: CoroutineExceptionHandler) {
//        viewModelScope.launch(handler) {
//            val list = AppDatabase.getInstance(getApplication()).DaoVaccinations().getAllRecords()
//            val forAdapter = ArrayList<VaccinationModel>()
//            for (t in list){
//                forAdapter.add(t.toMap())
//            }
//            latestList.value = forAdapter
//        }
//    }
//
//    fun onAdd(handler: CoroutineExceptionHandler, model: VaccinationModel): Boolean{
//        val success = addRoutine(model)
//        initList(handler)
//        return success
//    }
//
//    fun checkFields(code: String, series: String, lab: String, vacc:String, date: String): String?{
//        if (
//                vacc.isEmpty() ||
//                lab.isEmpty() ||
//                date.isEmpty() ||
//                code.isEmpty() ||
//                series.isEmpty() ||
//                Vaccines.getValue(vacc) == Vaccines.NONE
//        ) {
//            return "Fill in all the fields correctly"
//        }
//        if (AppDatabase.getInstance(getApplication()).DaoPerson().checkIfExists(code.toInt()) == 0){
//            return "There is no such person"
//        }
//        return null
//    }
//
//    private fun addRoutine(vaccinationModel: VaccinationModel): Boolean{
//        return try {
//            AppDatabase.getInstance(getApplication()).DaoVaccinations().addEntry(vaccinationModel.toMap())
//            true
//        } catch (e: Exception){
//            false
//        }
//
//    }
//}