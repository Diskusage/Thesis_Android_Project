package com.example.sqliteapp.ui.home

import android.app.Application
import android.graphics.Bitmap
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sqliteapp.database.DatabaseHelper
import com.example.sqliteapp.adapters.MyQrCodeEncoder
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val mText: MutableLiveData<String> = MutableLiveData()
    private val fText: MutableLiveData<String> = MutableLiveData()
    private val fCode: MutableLiveData<Bitmap> = MutableLiveData()
    private val sCode: MutableLiveData<Bitmap> = MutableLiveData()
    private val fName: MutableLiveData<String> = MutableLiveData()
    private val sName: MutableLiveData<String> = MutableLiveData()
    private val iDnp: MutableLiveData<String> = MutableLiveData()
    private var databaseHelper: DatabaseHelper? = null

    val firstCode: LiveData<Bitmap>
        get() = fCode
    val secondCode: LiveData<Bitmap>
        get() = sCode
    val text: LiveData<String>
        get() = mText
    val text2: LiveData<String>
        get() = fText
    val fNameGraph: LiveData<String>
        get() = fName
    val sNameGraph: LiveData<String>
        get() = sName
    val iDnpGraph: LiveData<String>
        get() = iDnp

    init {
        mText.value = "Latest vaccine QR code"
        fText.value = "Latest test QR code"
    }

    fun generateQrCodes(extras: Bundle?) {
        databaseHelper = DatabaseHelper(getApplication())
        fName.value = extras?.getString("FName")
        sName.value = extras?.getString("SName")
        iDnp.value = extras?.getString("IDNP")
        val toReturn = extras?.getString("IDNP")?.let { databaseHelper?.getLastTest(it) }
        val testRes = extras?.get("Background")
        var testQr:String ?= null
        var secondQr:String ?= null
        if (extras?.get("VaccinationType") != null) {
             testQr = "COVID-19 Vaccination details\n" +
                    "IDNP: ${extras.getString("IDNP")}\n" +
                    "Vaccine type: ${extras.get("VaccinationType")}\n" +
                    "Vaccination date: ${extras.getString("VaccinationDate")}"
        }
        if (toReturn != null) {
            secondQr = "COVID-19 Test details\n" +
                    "IDNP: ${extras.getString("IDNP")}\n" +
                    "Test date: ${toReturn[1]}\n" +
                    "Test result: ${if (testRes == true) "Positive" else "Negative"}\n" +
                    "Antibodies: "+
                    if (toReturn[0].equals("1")) "Present" else "Absent"
        }
        val writer = MultiFormatWriter()
        try {
            val bce = MyQrCodeEncoder()
            if (testQr != null) {
                val bm1 = writer.encode(testQr, BarcodeFormat.QR_CODE, 300, 300)
                val bitmap = if (testRes == null) {
                    bce.createBitmap(bm1)
                } else {
                    bce.createBitmap(bm1, testRes as Boolean?)
                }
                fCode.value = bitmap
            }
            if (secondQr != null) {
                val bm2 = writer.encode(secondQr, BarcodeFormat.QR_CODE, 300, 300)
                val bitmap2 = if (testRes == null) {
                    bce.createBitmap(bm2)
                } else {
                    bce.createBitmap(bm2, testRes as Boolean?)
                }
                sCode.value = bitmap2
            }
        }
        catch (e: WriterException) {
        }
    }


}