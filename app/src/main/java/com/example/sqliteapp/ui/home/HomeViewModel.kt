package com.example.sqliteapp.ui.home

import android.app.Application
import android.graphics.Bitmap
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sqliteapp.database.DatabaseHelper
import com.example.sqliteapp.database.MyEncoder
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val mText: MutableLiveData<String> = MutableLiveData()
    private val fText: MutableLiveData<String> = MutableLiveData()
    private val fCode: MutableLiveData<Bitmap> = MutableLiveData()
    private val sCode: MutableLiveData<Bitmap> = MutableLiveData()
    private var databaseHelper: DatabaseHelper? = null

    val firstCode: LiveData<Bitmap>
        get() = fCode
    val secondCode: LiveData<Bitmap>
        get() = sCode
    val text: LiveData<String>
        get() = mText
    val text2: LiveData<String>
        get() = fText

    init {
        mText.value = "Latest vaccine QR code"
        fText.value = "Latest test QR code"
    }

    fun generateQrCodes(extras: Bundle?) {
        databaseHelper = DatabaseHelper(getApplication())
        val toReturn = extras?.getString("IDNP")?.let { databaseHelper?.getLastTest(it) }
        val testRes = extras?.getBoolean("Background")
        val testQr = "COVID-19 Vaccination details\n" +
                "${extras?.getString("IDNP")} " +
                "${extras?.getString("VaccinationType")}" +
                " ${extras?.getString("VaccinationDate")}"
        //idnp date result anti
        val secondQr = "COVID-19 Test details\n" +
                "${extras?.getString("IDNP")} " +
                "${toReturn?.get(1)} " +
                "${if (testRes == true) "Positive" else "Negative"} " +
                if (toReturn?.get(0).equals("1")) "Antibodies present" else "No antibodies"
        val writer = MultiFormatWriter()
        try {
            val bm1 = writer.encode(testQr, BarcodeFormat.QR_CODE, 300, 300)
            val bm2 = writer.encode(secondQr, BarcodeFormat.QR_CODE, 300, 300)
            val bce = MyEncoder()
            val bitmap = if (testRes == null){
                bce.createBitmap(bm1)
            } else {
                bce.createBitmap(bm1, testRes)
            }
            val bitmap2 = if (testRes == null){
                bce.createBitmap(bm2)
            } else {
                bce.createBitmap(bm2, testRes)
            }

            fCode.value = bitmap
            sCode.value = bitmap2
        }
        catch (e: WriterException) {
        }
    }


}