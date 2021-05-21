package com.app.greenpass.util

import android.content.Context
import android.os.Looper
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineExceptionHandler

class CoroutineHelper(private val activity: Context?) {
    val handler = CoroutineExceptionHandler{_, exception ->
        if (Looper.myLooper() == null) {
            Looper.prepare()
            Looper.loop()
        }
        Log.i("Coroutine:", "Error", exception)
        Toast.makeText(activity, "Error: ${exception.cause}", Toast.LENGTH_SHORT).show()
    }
}