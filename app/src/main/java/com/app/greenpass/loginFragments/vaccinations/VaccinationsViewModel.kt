package com.app.greenpass.loginFragments.vaccinations

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.greenpass.database.AppDatabase
import com.app.greenpass.database.toMap
import com.app.greenpass.models.PersonModel
import com.app.greenpass.models.VaccinationModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder

//MVVM architecture for fragments
//functions, processes and interactions with model
//to transfer data back to view
class VaccinationsViewModel(application: Application) : AndroidViewModel(application) {
    private val mText: MutableLiveData<String> = MutableLiveData()
    private val descText: MutableLiveData<String> = MutableLiveData()
    private var qrPopup: MutableLiveData<Bitmap> = MutableLiveData()
    private var idnp: String? = null
    private lateinit var person: PersonModel

    val desc: LiveData<String>
        get() = descText
    val qr: LiveData<Bitmap>
        get() = qrPopup
    val text: LiveData<String>
        get() = mText

    init {
        mText.value = "History of vaccinations"
        descText.value = "QR code for this vaccination"
    }

    fun getKey(key: Int){
        person = AppDatabase.getInstance(getApplication()).DaoPerson().getPerson(key).toMap()
        idnp = person.iDNP
    }

    fun generateQrCode(vaccination: VaccinationModel){
        val qrCode = "COVID-19 Vaccination details\n" +
                vaccination.toString()
        try{
            val writer = MultiFormatWriter()
            val bm = writer.encode(qrCode, BarcodeFormat.QR_CODE, 500, 500)
            val bce = BarcodeEncoder()
            val bitmap = bce.createBitmap(bm)
            qrPopup.value = bitmap
        } catch (e: WriterException){
            e.printStackTrace()
        }
    }


    fun getDataForAdapter(): MutableList<VaccinationModel> {
        val list = AppDatabase.getInstance(getApplication()).DaoVaccinations().getRecordsForPerson(person.hashCode())
        val toRet = ArrayList<VaccinationModel>()
        for (t in list){
            toRet.add(t.toMap())
        }
        return toRet
    }
}