package com.app.greenpass.database.daos

import android.content.ContentValues
import android.util.Log
import com.app.greenpass.database.dataclasses.Tests
import com.app.greenpass.database.dataclasses.Vaccinations
import com.app.greenpass.enums.Vaccines
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

class DaoFireStoreVaccination(private val db: FirebaseFirestore) {

    suspend fun getRecordsForPerson(idnp: Int): List<Vaccinations>? {
        var ok : ArrayList<Vaccinations>? = null
        val data = db.collection("vaccinations").get().await()
        if (data != null) {
            ok = ArrayList()
            for (user in data){
                user["owner"]?.let {
                    if (it == idnp.toLong()){
                        ok.add(
                            Vaccinations(
                                personCode = user["owner"].toString().toInt(),
                                series = user["series"] as String,
                                labName = user["laboratory"] as String,
                                vaccineType = Vaccines.getValue(user["type"] as String).t,
                                vaccinationDate = user["date"] as String
                            )
                        )
                    }
                }
            }
            ok.sort()
        }
        return ok
    }

    suspend fun getLastVaccination(indp: Int): Vaccinations? {
        return getRecordsForPerson(indp)?.last()
    }

    suspend fun <T> Task<T>.await(): T? {
        if (isComplete) {
            val e = exception
            return if (e == null) {
                if (isCanceled) {
                    throw CancellationException(
                        "Task $this was cancelled normally.")
                } else {
                    result
                }
            } else {
                throw e
            }
        }

        return suspendCancellableCoroutine { cont ->
            addOnCompleteListener {
                val e = exception
                if (e == null) {
                    if (isCanceled) cont.cancel() else cont.resume(result, {
                        it.printStackTrace()
                    })
                } else {
                    cont.resumeWithException(e)
                }
            }
        }
    }
}