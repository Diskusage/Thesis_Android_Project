package com.app.greenpass.loginFragments.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.app.greenpass.R
import com.app.greenpass.database.daos.DaoTest
import com.app.greenpass.database.dataclasses.*
import com.app.greenpass.models.PersonModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

//MVVM architecture for fragments
//functions, processes and interactions with model
//to transfer data back to view
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
class ProfileViewModel(application: Application) : BaseViewModel(application) {
    var lastState: State? = null

    fun updateView(person: PersonModel) {
        this.person = person
        val state = ViewResult.Authorized(person)
        generateQrCodes(state)
    }

    private fun generateQrCodes(state: ViewResult.Authorized) {
        viewModelScope.launch(helper.handler) {
            val jobOne = async {
                val toRet = roomDb.DaoTest().getLastTestForPerson(person.hashCode())?.toMap() ?: let {
                    val toSave = fireDb.daoTest().getLastTestForPerson(person.hashCode())?.toMap()
                    toSave?.let { roomDb.DaoTest().saveTest(it.toMap()) }
                    toSave
                }
                toRet
            }
            val jobTwo = async {
                val toRet = roomDb.DaoVaccinations().getLastVaccination(person.hashCode())?.toMap() ?: let {
                    val toSave = fireDb.daoVaccinations().getLastVaccination(person.hashCode())?.toMap()
                    toSave?.let { roomDb.DaoVaccinations().saveVaccination(it.toMap()) }
                    toSave
                }

                toRet
            }
            val testQr: String? = if (jobTwo.await() != null)
                getApplication<Application>().resources.getString(R.string.vacc_details) +
                        jobTwo.await()?.display()
            else null
            val secondQr: String? = if (jobOne.await() != null)
                getApplication<Application>().resources.getString(R.string.test_details) +
                        jobOne.await()?.display()
            else null
            val writer = MultiFormatWriter()
            val bce = BarcodeEncoder()
            val stateFirst = secondQr?.let {
                ViewResult.LoadedFirst(
                    bce.createBitmap(writer.encode(secondQr, BarcodeFormat.QR_CODE, 300, 300))
                )
            }
            val stateSecond = testQr?.let {
                ViewResult.LoadedSecond(
                    bce.createBitmap(writer.encode(testQr, BarcodeFormat.QR_CODE, 300, 300))
                )
            }
            val finalState = ViewResult.FinishLoad(
                Pair(stateFirst, stateSecond),
                state,
                jobOne.await()?.testDate,
                jobTwo.await()?.vaccDate
            )
            viewResult.postValue(finalState)
            lastState = finalState
        }
    }

    sealed class ViewResult : State {
        class FinishLoad(
            val qrData: Pair<LoadedFirst?, LoadedSecond?>,
            val stringData: Authorized,
            val dateTest: String?,
            val dateVaccination: String?
        ) : ViewResult()

        class LoadedSecond(
            val vaccMap: Bitmap?,
        ) : ViewResult()

        class LoadedFirst(
            val testMap: Bitmap?,
        ) : ViewResult()

        class Authorized(
            val person: PersonModel,
        ) : ViewResult()
    }
}
