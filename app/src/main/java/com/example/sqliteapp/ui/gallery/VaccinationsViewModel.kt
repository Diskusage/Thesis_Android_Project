package com.example.sqliteapp.ui.gallery

import android.app.Application
import android.graphics.Bitmap
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sqliteapp.adapters.MyQrCodeEncoder
import com.example.sqliteapp.database.DatabaseHelper
import com.example.sqliteapp.models.PersonModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException

class VaccinationsViewModel(application: Application) : AndroidViewModel(application) {
    private val mText: MutableLiveData<String> = MutableLiveData()
    private val descText: MutableLiveData<String> = MutableLiveData()
    private var qrPopup: MutableLiveData<Bitmap> = MutableLiveData()
    private var idnp: String? = null
    private var databaseHelper: DatabaseHelper? = null
    private var backgroundRed: Boolean? = null

    val desc: LiveData<String>
        get() = descText
    val qr: LiveData<Bitmap>
        get() = qrPopup
    val text: LiveData<String>
        get() = mText

    init {
        databaseHelper = DatabaseHelper(getApplication())
        mText.value = "History of vaccinations"
        descText.value = "QR code for this vaccination"
    }

    fun initList(extras: Bundle?, backgroundRed: Boolean?){
        idnp = extras?.getString("IDNP")
        this.backgroundRed = backgroundRed
    }

    fun generateQrCode(person: PersonModel?){
        val qrCode = "COVID-19 Vaccination details\n" +
                "IDNP: ${person?.iDNP}\n" +
                "Vaccine type: ${person?.type}\n" +
                "Vaccination date: ${person?.vaccDate}"
        try{
            val writer = MultiFormatWriter()
            val bm = writer.encode(qrCode, BarcodeFormat.QR_CODE, 500, 500)
            val bce = MyQrCodeEncoder()
            val bitmap = backgroundRed?.let { bce.createBitmap(bm, it) }
            qrPopup.value = bitmap
        } catch (e: WriterException){
            e.printStackTrace()
        }
    }


    fun getDataForAdapter(): MutableList<PersonModel>? {
        return databaseHelper?.getAll1Person(idnp)
    }
}