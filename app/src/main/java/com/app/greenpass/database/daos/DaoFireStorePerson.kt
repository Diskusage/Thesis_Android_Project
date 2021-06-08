package com.app.greenpass.database.daos

import com.app.greenpass.database.dataclasses.People
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

class DaoFireStorePerson(private val db: FirebaseFirestore) {

    suspend fun getPerson(code: Int): People?{
        var ok : People? = null
        val data = db.collection("users").get().await()
        if (data != null){
            for (user in data){
                if (user["code"] == code.toLong()){
                    ok = People(
                        user["name"] as String,
                        user["lastname"] as String,
                        user["idnp"].toString(),
                        user["code"].toString().toInt()
                    )
                }
            }
        }
        return ok
//                .addOnSuccessListener { result ->
//                    for (user in result){
//                        user["code"]?.let {
//                            if (it == code.toLong()){
//                                ok = People(
//                                    user["name"] as String,
//                                    user["lastname"] as String,
//                                    user["idnp"].toString(),
//                                    user["code"].toString().toInt()
//                                )
//                                flag = false
//                                return@addOnSuccessListener
//                            }
//                        }
//                    }
//                    flag = false
//                }
//                .addOnFailureListener { result ->
//                    Log.d(TAG, "Didn't get collection $result")
//                    flag = false
//                }

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