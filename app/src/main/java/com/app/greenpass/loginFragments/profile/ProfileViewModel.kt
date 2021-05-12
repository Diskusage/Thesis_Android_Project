package com.app.greenpass.loginFragments.profile

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.greenpass.database.AppDatabase
import com.app.greenpass.database.toMap
import com.app.greenpass.models.PersonModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

//MVVM architecture for fragments
//functions, processes and interactions with model
//to transfer data back to view
class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val mText: MutableLiveData<String> = MutableLiveData()
    private val fText: MutableLiveData<String> = MutableLiveData()
    private val fCode: MutableLiveData<Bitmap> = MutableLiveData()
    private val sCode: MutableLiveData<Bitmap> = MutableLiveData()
    private val fName: MutableLiveData<String> = MutableLiveData()
    private val sName: MutableLiveData<String> = MutableLiveData()
    private val pCode: MutableLiveData<String> = MutableLiveData()
    private val iDnp: MutableLiveData<String> = MutableLiveData()
    private lateinit var person: PersonModel

    val persCode: LiveData<String>
        get() = pCode
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
        mText.value = "No vaccinations found"
        fText.value = "No tests found"
    }

    fun getKey(key: Int){
        person = AppDatabase.getInstance(getApplication()).DaoPerson().getPerson(key).toMap()
        iDnp.postValue(person.iDNP)
        fName.postValue(person.firstName)
        sName.postValue(person.secondName)
        pCode.postValue(person.hashCode().toString())
    }

    fun generateQrCodes(handler: CoroutineExceptionHandler) {
        runBlocking(handler) {
            val jobOne = async(Dispatchers.Default) {
                val lastTest = AppDatabase.getInstance(getApplication()).DaoTest().getLastTestForPerson(person.hashCode())
                lastTest?.toMap()
            }
            val jobTwo = async(Dispatchers.Default) {
                val lastVaccination = AppDatabase.getInstance(getApplication()).DaoVaccinations().getLastVaccination(person.hashCode())
                lastVaccination?.toMap()
            }
            var testQr:String? = null
            var secondQr:String? = null
            if (jobTwo.await() != null) {
                 testQr = "COVID-19 VACCINATION DETAILS:\n" +
                        jobTwo.await().toString()
            }
            if (jobOne.await() != null){
                secondQr = "COVID-19 Test details\n" +
                        jobOne.await().toString()
            }
            val writer = MultiFormatWriter()
            val bce = BarcodeEncoder()
            if (testQr != null){
                val bm1 = writer.encode(testQr, BarcodeFormat.QR_CODE, 300, 300)
                val bitmap = bce.createBitmap(bm1)
                fCode.value = bitmap
                mText.value = "Latest vaccine QR code"
            }
            if (secondQr != null){
                val bm2 = writer.encode(secondQr, BarcodeFormat.QR_CODE, 300, 300)
                val bitmap2 = bce.createBitmap(bm2)
                sCode.value = bitmap2
                fText.value = "Latest test QR code"
            }

        }
    }
}