package com.example.sqliteapp.ui.tests

import android.app.Application
import android.graphics.Bitmap
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sqliteapp.database.DatabaseHelper
import com.example.sqliteapp.database.MyEncoder
import com.example.sqliteapp.models.TestModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException

class TestsViewModel(application: Application) : AndroidViewModel(application) {
    private var qrCode: MutableLiveData<Bitmap> = MutableLiveData()
    private val description: MutableLiveData<String> = MutableLiveData()
    private val mText: MutableLiveData<String> = MutableLiveData()
    private var idnp: String? = null
    private var databaseHelper: DatabaseHelper? = null
    private var backgroundRed: Boolean? = null
    val text: LiveData<String>
        get() = mText
    val qr: LiveData<Bitmap>
        get() = qrCode
    val desc: LiveData<String>
        get() = description
    init {
        databaseHelper = DatabaseHelper(getApplication())
        mText.value = "Test history"
        description.value = "QR code for this test"
    }

    fun initList(extras: Bundle?, backgroundRed: Boolean?){
        idnp = extras?.getString("IDNP")
        this.backgroundRed = backgroundRed
    }

    fun getDataForAdapter(): List<TestModel> {
        return databaseHelper!!.getAllTests1Person(idnp!!)
    }

    fun generateQrCode(clickedTest: TestModel) {
        val testQr = "${clickedTest.idnp} ${clickedTest.testDate} " +
                "${if (clickedTest.testResult) "Positive" else "Negative"} " +
                if (clickedTest.isAntibodies) "Antibodies present" else "Antibodies absent"
        try {
            val writer = MultiFormatWriter()
            val bm = writer.encode(testQr, BarcodeFormat.QR_CODE, 350, 350)
            val bce = MyEncoder()
            val bitmap = backgroundRed?.let { bce.createBitmap(bm, it) }
            qrCode.value = bitmap
        } catch (e: WriterException){
            e.printStackTrace()
        }
    }
}