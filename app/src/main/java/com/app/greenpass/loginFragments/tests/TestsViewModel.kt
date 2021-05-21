package com.app.greenpass.loginFragments.tests

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.greenpass.R
import com.app.greenpass.database.AppDatabase
import com.app.greenpass.database.toMap
import com.app.greenpass.models.PersonModel
import com.app.greenpass.models.TestModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder

//MVVM architecture for fragments
//functions, processes and interactions with model
//to transfer data back to view
class TestsViewModel(application: Application) : AndroidViewModel(application) {
    private val viewResult : MutableLiveData<ViewResult> = MutableLiveData()
    val viewsResult : LiveData<ViewResult>
        get() = viewResult
    private lateinit var personModel: PersonModel
    fun getKey(key: Int){
        personModel = AppDatabase.getInstance(getApplication()).DaoPerson().getPerson(key).toMap()
        viewResult.postValue(ViewResult.Opened(
            R.string.tHistory,
            AppDatabase
                .getInstance(getApplication())
                .DaoTest()
                .getAllTestForPerson(
                    personModel.hashCode()
                )
                .map { it.toMap() }
        ))
    }
    fun updateView(){
    viewResult.postValue(ViewResult.Opened(
        R.string.tHistory,
        AppDatabase
            .getInstance(getApplication())
            .DaoTest()
            .getAllTestForPerson(
                personModel.hashCode()
            )
            .map { it.toMap() }
    ))
    }
    fun generateQrCode(clickedTest: TestModel) {
        val testQr = getApplication<Application>().resources.getString(R.string.test_details) +
                clickedTest.toString()
        try {
            val writer = MultiFormatWriter()
            val bce = BarcodeEncoder()
            viewResult.postValue(ViewResult.Generated(
                R.string.this_test,
                bce.createBitmap(writer.encode(testQr, BarcodeFormat.QR_CODE, 350, 350))
            ))
        } catch (e: WriterException){
            e.printStackTrace()
        }
    }
    sealed class ViewResult {
        class  Generated(
            val testText: Int,
            val testMap: Bitmap?,
        ) : ViewResult()
        class  Opened(
            val text: Int,
            val list: List<TestModel>,
        ) : ViewResult()
    }
}