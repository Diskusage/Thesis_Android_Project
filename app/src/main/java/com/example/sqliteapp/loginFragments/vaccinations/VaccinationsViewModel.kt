package com.example.sqliteapp.loginFragments.vaccinations

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sqliteapp.database.AppDatabase
import com.example.sqliteapp.database.toMap
import com.example.sqliteapp.models.PersonModel
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
class VaccinationsViewModel(application: Application) : AndroidViewModel(application) {
    private val mText: MutableLiveData<String> = MutableLiveData()
    private val descText: MutableLiveData<String> = MutableLiveData()
    private var qrPopup: MutableLiveData<Bitmap> = MutableLiveData()
    private var idnp: String? = null

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

    fun getKey(key: String){
        idnp = key
    }

    fun generateQrCode(person: PersonModel?){
        val qrCode = "COVID-19 Vaccination details\n" +
                "IDNP: ${person?.iDNP}\n" +
                "Vaccine type: ${person?.type}\n" +
                "Vaccination date: ${person?.vaccDate}"
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


    fun getDataForAdapter(handler: CoroutineExceptionHandler): ArrayList<PersonModel> {
        val toRet = ArrayList<PersonModel>()
        runBlocking(handler) {
            val get = async(Dispatchers.Default) {
                AppDatabase.getInstance(getApplication()).DaoPerson().getRecordsForPerson(idnp)
            }
            for (t in get.await()){
                toRet.add(t.toMap())
            }
        }
        return toRet
    }
}