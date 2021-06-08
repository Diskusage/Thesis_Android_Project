package com.app.greenpass.loginFragments.viewmodels

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.app.greenpass.R
import com.app.greenpass.database.dataclasses.toMap
import com.app.greenpass.models.PersonModel
import com.app.greenpass.models.TestModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.launch

//MVVM architecture for fragments
//functions, processes and interactions with model
//to transfer data back to view

class TestsViewModel(application: Application) : BaseViewModel(application) {

    fun getKey(key: PersonModel){
        person = key
    }
    fun updateView(){
        viewModelScope.launch {
            viewResult.postValue(fireDb
                .daoTest()
                .getAllTestForPerson(person.hashCode())?.let { it ->
                    ViewResult.Opened(
                        R.string.tHistory,
                        it
                            .map { it.toMap() }
                    )
                })
        }
    }
    fun generateQrCode(clickedTest: TestModel) {
        viewModelScope.launch {
            val testQr = getApplication<Application>().resources.getString(R.string.test_details) +
                    clickedTest.display()
            try {
                val writer = MultiFormatWriter()
                val bce = BarcodeEncoder()
                viewResult.postValue(
                    ViewResult.Generated(
                        R.string.this_test,
                        bce.createBitmap(writer.encode(testQr, BarcodeFormat.QR_CODE, 350, 350)),
                        clickedTest.testDate
                    )
                )
            } catch (e: WriterException){
                e.printStackTrace()
            }
        }
    }
    sealed class ViewResult : State{
        class  Generated(
            val testText: Int,
            val testMap: Bitmap?,
            val date: String
        ) : ViewResult()
        class  Opened(
            val text: Int,
            val list: List<TestModel>,
        ) : ViewResult()
    }
}