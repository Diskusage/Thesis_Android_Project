package com.example.sqliteapp.loginFragments.tests

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sqliteapp.database.AppDatabase
import com.example.sqliteapp.database.toMap
import com.example.sqliteapp.models.TestModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

//MVVM architecture for fragments
//functions, processes and interactions with model
//to transfer data back to view
class TestsViewModel(application: Application) : AndroidViewModel(application) {
    private var qrCode: MutableLiveData<Bitmap> = MutableLiveData()
    private val description: MutableLiveData<String> = MutableLiveData()
    private val mText: MutableLiveData<String> = MutableLiveData()
    private var idnp: String? = null


    val text: LiveData<String>
        get() = mText
    val qr: LiveData<Bitmap>
        get() = qrCode
    val desc: LiveData<String>
        get() = description
    init {
        mText.value = "Test history"
        description.value = "QR code for this test"
    }

    fun getKey(key: String){
        idnp = key
    }

    fun getDataForAdapter(handler: CoroutineExceptionHandler): MutableList<TestModel> {
        val toRet = ArrayList<TestModel>()
        runBlocking (handler){
            val get = async(Dispatchers.Default) {
                AppDatabase.getInstance(getApplication()).DaoTest().getAllTestForPerson(idnp)
            }
            for (t in get.await()){
                toRet.add(t.toMap())
            }
        }
        return toRet
    }

    fun generateQrCode(clickedTest: TestModel?) {
        val testQr = "COVID-19 Test details\n" +
                "IDNP: ${clickedTest?.idnp}\n" +
                "Test date: ${clickedTest?.testDate}\n" +
                "Test result: ${if (clickedTest?.testResult == true) "Positive" else "Negative"}\n" +
                "Antibodies: " +
                if (clickedTest?.isAntibodies == true) "Present" else "Absent"
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