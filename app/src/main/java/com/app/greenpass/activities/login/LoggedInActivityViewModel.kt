package com.app.greenpass.activities.login

import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import com.app.greenpass.database.AppDatabase
import com.app.greenpass.database.toMap
import com.app.greenpass.loginFragments.profile.ProfileViewModel
import com.app.greenpass.loginFragments.tests.TestsViewModel
import com.app.greenpass.loginFragments.vaccinations.VaccinationsViewModel
import com.app.greenpass.models.PersonModel

class LoggedInActivityViewModel(application: Application): AndroidViewModel(application) {
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun initFragments(bundle: Bundle?,
                      fViewModel: ProfileViewModel,
                      sViewModel: VaccinationsViewModel,
                      lViewModel: TestsViewModel){
        val idnp = bundle?.getInt("user")
        if (idnp != null) {
            fViewModel.getKey(idnp)
            sViewModel.getKey(idnp)
            lViewModel.getKey(idnp)
        }

    }
    fun fetchPerson(code: Int?): PersonModel?{
        return code?.let { AppDatabase.getInstance(getApplication()).DaoPerson().getPerson(it).toMap() }
    }

}