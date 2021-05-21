package com.app.greenpass.loginFragments.profile

import android.app.Application
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.greenpass.R
import com.app.greenpass.database.AppDatabase
import com.app.greenpass.database.toMap
import com.app.greenpass.models.PersonModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.*

//MVVM architecture for fragments
//functions, processes and interactions with model
//to transfer data back to view
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var person: PersonModel
    private val viewResult : MutableLiveData<ViewResult> = MutableLiveData()
    val viewsResult : LiveData<ViewResult>
        get() = viewResult

    fun getKey(key: Int){
        person = AppDatabase.getInstance(getApplication()).DaoPerson().getPerson(key).toMap()
        updateView()
    }

    fun updateView(){
        viewResult.postValue(ViewResult.Authorized(person))
    }

    fun generateQrCodes(handler: CoroutineExceptionHandler) {
        runBlocking(handler) {
            val jobOne = async(Dispatchers.IO) {
                AppDatabase.getInstance(getApplication()).DaoTest().getLastTestForPerson(person.hashCode())?.toMap()
            }
            val jobTwo = async(Dispatchers.IO) {
                AppDatabase.getInstance(getApplication()).DaoVaccinations().getLastVaccination(person.hashCode())?.toMap()
            }
            val testQr:String? = if (jobTwo.await() != null)
                getApplication<Application>().resources.getString(R.string.vacc_details) +
                        jobTwo.await().toString()
            else null
            val secondQr:String? = if (jobOne.await() != null)
                getApplication<Application>().resources.getString(R.string.test_details) +
                        jobOne.await().toString()
            else null
            val writer = MultiFormatWriter()
            val bce = BarcodeEncoder()
            delay(500)
            viewResult.postValue(
                ViewResult.FinishLoad(
                    Pair(
                        secondQr?.let {
                                ViewResult.LoadedFirst(
                                    R.string.latest_test_qr,
                                    bce.createBitmap(
                                        writer.encode(
                                            secondQr, BarcodeFormat.QR_CODE, 300, 300
                                        )
                                    )
                                )
                        },
                        testQr?.let {
                            ViewResult.LoadedSecond(
                                R.string.latest_vacc_qr,
                                bce.createBitmap(
                                    writer.encode(
                                        testQr, BarcodeFormat.QR_CODE, 300, 300
                                    )
                                )
                            )
                        }
                    )
                )
            )
        }
    }

    sealed class ViewResult {
        class  FinishLoad(
            val data: Pair<LoadedFirst?, LoadedSecond?>
        ):ViewResult()
        class  LoadedSecond(
            val vaccText: Int,
            val vaccMap: Bitmap?,
        ) : ViewResult()
        class  LoadedFirst(
           val testText: Int,
           val testMap: Bitmap?,
        ) : ViewResult()
        class  Authorized(
           val person: PersonModel,
        ) : ViewResult()
    }
}
