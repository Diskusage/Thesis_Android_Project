package com.app.greenpass.loginFragments.views

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.app.greenpass.R
import com.app.greenpass.databinding.FragmentProfileBinding
import com.app.greenpass.loginFragments.viewmodels.ProfileViewModel
import com.app.greenpass.loginFragments.viewmodels.State
import com.app.greenpass.models.PersonModel

//MVVM architecture for fragments
//a profile view, consists of layout bindings and
//listeners for user interaction
open class ProfileView : Fragment() {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var mBinding : FragmentProfileBinding
    private val binding get() = mBinding
    private var lastTest : String? = null
    private var lastVaccination : String? = null
    private var testCache : Bitmap? = null
    private var vaccinationCache : Bitmap? = null
    private var personCache : PersonModel? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

            profileViewModel.viewsResult.observe(
                viewLifecycleOwner,
                { s -> processState(s)}
            )
            binding.switch1.setOnCheckedChangeListener { _, isChecked ->
                switchUpdate(isChecked)
            }

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onStart() {
        profileViewModel.lastState?.let { processState(it) }
        super.onStart()
    }

    override fun onResume() {
        super.onResume()

        personCache?.let {
            readPerson(it)
        }
    }

    private fun readPerson(personModel: PersonModel){
        binding.firstNameGraph.text = personModel.firstName
        binding.secondNameGraph.text = personModel.secondName
        binding.idnpGraph.text = personModel.iDNP
        binding.idnpGraph2.text = (personModel.firstName+personModel.secondName+personModel.iDNP).hashCode().toString()
        switchUpdate(binding.switch1.isChecked)
    }

    private fun switchUpdate(checked: Boolean){
        if (checked){
            binding.qrCode.setImageBitmap(testCache)
            binding.textHome.text = resources.getString(R.string.latest_test_qr, lastTest)
        }
        else {
            binding.qrCode.setImageBitmap(vaccinationCache)
            binding.textHome.text = resources.getString(R.string.latest_vacc_qr, lastVaccination)
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun processState(state : State){
        when(state){
//            is ProfileViewModel.ViewResult.Authorized -> state.person?.apply {
//                readPerson(this)
//                personCache = this
//                profileViewModel.generateQrCodes()
//            }
            is ProfileViewModel.ViewResult.FinishLoad -> state.apply {
                readPerson(stringData.person)
                personCache = stringData.person
                vaccinationCache = qrData.second?.vaccMap
                testCache = qrData.first?.testMap
                lastVaccination = dateVaccination
                lastTest = dateTest
                switchUpdate(binding.switch1.isChecked)
            }
        }
    }

    }

