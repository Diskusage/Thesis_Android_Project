package com.app.greenpass.loginFragments.profile

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.app.greenpass.databinding.FragmentProfileBinding
import com.app.greenpass.util.CoroutineHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//MVVM architecture for fragments
//a profile view, consists of layout bindings and
//listeners for user interaction
open class ProfileView : Fragment() {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var mBinding : FragmentProfileBinding
    private val binding get() = mBinding
    private val handler = CoroutineHelper(context).handler
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        profileViewModel.viewsResult.observe(
            viewLifecycleOwner,
            {s -> when(s){
                 is ProfileViewModel.ViewResult.Authorized -> s.person.apply {
                     binding.firstNameGraph.text = firstName
                     binding.secondNameGraph.text = secondName
                     binding.idnpGraph.text = iDNP
                     binding.idnpGraph2.text = hashCode().toString()
                 }
                is ProfileViewModel.ViewResult.FinishLoad -> s.data.apply {
                    binding.textTest.text = first?.testText?.let { resources.getString(it) }
                    binding.qrCode2.setImageBitmap(first?.testMap)
                    binding.textHome.text = second?.vaccText?.let { resources.getString(it) }
                    binding.qrCode.setImageBitmap(second?.vaccMap)
                }
                is ProfileViewModel.ViewResult.LoadedFirst -> s.apply {
                    binding.textTest.text = resources.getString(testText)
                    binding.qrCode2.setImageBitmap(testMap)
                }
                is ProfileViewModel.ViewResult.LoadedSecond -> s.apply {
                    binding.textHome.text = resources.getString(vaccText)
                    binding.qrCode.setImageBitmap(vaccMap)
                }
            } }
        )
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onResume() {
        super.onResume()
        GlobalScope.launch(Dispatchers.IO) {
            profileViewModel.generateQrCodes(handler)
        }
        profileViewModel.updateView()
    }

}