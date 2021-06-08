package com.app.greenpass.activities.login

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.app.greenpass.database.dataclasses.toMap
import com.app.greenpass.loginFragments.viewmodels.*
import com.app.greenpass.models.PersonModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoggedInActivityViewModel(application: Application): BaseViewModel(application) {
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun initFragments(
        user: PersonModel,
        fViewModel: ProfileViewModel,
        sViewModel: VaccinationsViewModel,
        lViewModel: TestsViewModel
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            fViewModel.updateView(user)
            sViewModel.getKey(user)
            lViewModel.getKey(user)
        }
    }

     fun fetchPerson(code: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val data = fireDb.daoPerson().getPerson(code)?.toMap()
            viewResult.postValue(ViewResult.Downloaded(data))
        }
    }

    sealed class ViewResult : State {
        class Downloaded(
            val person: PersonModel?,
        ) : ViewResult()
    }
}