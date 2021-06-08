package com.app.greenpass.loginFragments.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.app.greenpass.models.PersonModel

class PersonLayer(application: Application) : AndroidViewModel(application) {
    protected val person : PersonModel? = null
}