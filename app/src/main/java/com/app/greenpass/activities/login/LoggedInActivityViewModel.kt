package com.app.greenpass.activities.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.app.greenpass.loginFragments.profile.ProfileViewModel
import com.app.greenpass.loginFragments.tests.TestsViewModel
import com.app.greenpass.loginFragments.vaccinations.VaccinationsViewModel

class LoggedInActivityViewModel(application: Application): AndroidViewModel(application) {
    fun initFragments(idnp: String?,
                      sKey: String?,
                      lKey: String?,
                      fViewModel: ProfileViewModel,
                      sViewModel: VaccinationsViewModel,
                      lViewModel: TestsViewModel){
        if (idnp != null){
            fViewModel.getKey(idnp, sKey, lKey)
            sViewModel.getKey(idnp)
            lViewModel.getKey(idnp)
        }
    }
}