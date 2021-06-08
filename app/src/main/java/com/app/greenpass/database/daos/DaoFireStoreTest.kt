package com.app.greenpass.database.daos

import com.app.greenpass.database.dataclasses.Tests
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.resumeWithException

class DaoFireStoreTest(private val db : FirebaseFirestore)  {


    suspend fun getLastTestForPerson(idnp: Int): Tests? {
        return getAllTestForPerson(idnp)?.last()
    }

    suspend fun getAllTestForPerson(idnp: Int): List<Tests>? {
        var ok : ArrayList<Tests>? = null
        val data = db.collection("tests").get().await()
        if (data != null) {
            ok = ArrayList()
            for (user in data){
                user["owner"]?.let {
                        if (it == idnp.toLong()){
                            ok.add(Tests(
                                personCode = user["owner"].toString().toInt(),
                                testDate = user["date"] as String,
                                testResult = user["result"] as Boolean,
                                antibodies = user["antibodies"] as Boolean
                            ))
                        }
                    }
            }
            ok.sort()
        }
        return ok
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