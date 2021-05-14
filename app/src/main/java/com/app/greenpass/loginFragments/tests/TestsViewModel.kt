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
    private var qrCode: MutableLiveData<Bitmap> = MutableLiveData()
    private val description: MutableLiveData<Int> = MutableLiveData()
    private val mText: MutableLiveData<Int> = MutableLiveData()
    private lateinit var person: PersonModel
    private var idnp: String? = null


    val text: LiveData<Int>
        get() = mText
    val qr: LiveData<Bitmap>
        get() = qrCode
    val desc: LiveData<Int>
        get() = description

    fun getKey(key: Int){
        mText.postValue(R.string.tHistory)
        description.postValue(R.string.this_test)
        person = AppDatabase.getInstance(getApplication()).DaoPerson().getPerson(key).toMap()
        idnp = person.iDNP
    }

    fun getDataForAdapter(): MutableList<TestModel> {
        val list = AppDatabase.getInstance(getApplication()).DaoTest().getAllTestForPerson(person.hashCode())
        val toRet = ArrayList<TestModel>()
        for (t in list){
            toRet.add(t.toMap())
        }
        return toRet
    }

    fun generateQrCode(clickedTest: TestModel) {
        val testQr = getApplication<Application>().resources.getString(R.string.test_details) +
                clickedTest.toString()
        try {
            val writer = MultiFormatWriter()
            val bm = writer.encode(testQr, BarcodeFormat.QR_CODE, 350, 350)
            val bce = BarcodeEncoder()
            val bitmap = bce.createBitmap(bm)
            qrCode.value = bitmap
        } catch (e: WriterException){
            e.printStackTrace()
        }
    }
}