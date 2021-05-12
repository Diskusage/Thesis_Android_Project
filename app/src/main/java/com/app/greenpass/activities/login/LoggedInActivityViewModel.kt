package com.app.greenpass.activities.login

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import com.app.greenpass.loginFragments.profile.ProfileViewModel
import com.app.greenpass.loginFragments.tests.TestsViewModel
import com.app.greenpass.loginFragments.vaccinations.VaccinationsViewModel

class LoggedInActivityViewModel(application: Application): AndroidViewModel(application) {
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
}