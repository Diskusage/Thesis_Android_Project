package com.example.sqliteapp.activities.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.sqliteapp.loginFragments.profile.ProfileViewModel
import com.example.sqliteapp.loginFragments.tests.TestsViewModel
import com.example.sqliteapp.loginFragments.vaccinations.VaccinationsViewModel

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