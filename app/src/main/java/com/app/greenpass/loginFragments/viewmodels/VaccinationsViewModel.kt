package com.app.greenpass.loginFragments.viewmodels

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.app.greenpass.R
import com.app.greenpass.database.dataclasses.toMap
import com.app.greenpass.models.PersonModel
import com.app.greenpass.models.VaccinationModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.launch

//MVVM architecture for fragments
//functions, processes and interactions with model
//to transfer data back to view
class VaccinationsViewModel(application: Application) : BaseViewModel(application) {

    fun getKey(key: PersonModel) {
        person = key
    }

    fun updateView() {
        viewModelScope.launch(helper.handler) {
            val get = fireDb.daoVaccinations().getRecordsForPerson(person.hashCode())
            viewResult.postValue(get?.let { it ->
                ViewResult.Opened(
                    R.string.vHistory,
                    it.map { it.toMap() })
            })
        }
    }

    fun generateQrCode(vaccination: VaccinationModel) {
        viewModelScope.launch(helper.handler) {
            val qrCode = getApplication<Application>().resources.getString(R.string.vacc_details) +
                    vaccination.display()
            val writer = MultiFormatWriter()
            val bce = BarcodeEncoder()
            viewResult.postValue(
                ViewResult.Generated(
                    R.string.this_vacc,
                    bce.createBitmap(writer.encode(qrCode, BarcodeFormat.QR_CODE, 500, 500)),
                    vaccination.vaccDate
                )
            )
        }
    }

    sealed class ViewResult : State {
        class Generated(
            val testText: Int,
            val testMap: Bitmap?,
            val date: String
        ) : ViewResult()

        class Opened(
            val text: Int,
            val list: List<VaccinationModel>,
        ) : ViewResult()
    }
}