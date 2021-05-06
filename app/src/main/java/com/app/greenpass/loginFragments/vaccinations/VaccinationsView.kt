package com.app.greenpass.loginFragments.vaccinations

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.app.greenpass.adapters.ClickListener
import com.app.greenpass.adapters.GenerateQrCodeAdapter
import com.app.greenpass.databinding.FragmentVaccinationsBinding
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//MVVM architecture for fragments
//a vaccinations view, consists of layout bindings and
//listeners for user interaction
open class VaccinationsView : Fragment() {
    private val vaccinationsViewModel: VaccinationsViewModel by activityViewModels()
    private lateinit var mBinding : FragmentVaccinationsBinding
    private val binding get() = mBinding
    private val handler = CoroutineExceptionHandler{_, exception ->
        if (Looper.myLooper() == null) {
            Looper.prepare()
            Looper.loop()
        }
        Log.i("Coroutine:", "Error", exception)
        Toast.makeText(activity, "Error: ${exception.cause}", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentVaccinationsBinding.inflate(inflater)

        vaccinationsViewModel.text.observe(
                viewLifecycleOwner,
                { s -> mBinding.galleryText.text = s }
        )

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        showList()
    }

    private fun showList(){

        mBinding.vaccHistory.adapter = GenerateQrCodeAdapter(
                vaccinationsViewModel.getDataForAdapter(handler),
                object : ClickListener {
                    override fun onPositionClicked(i: Int) {
                        vaccinationsViewModel.desc.observe(
                                viewLifecycleOwner,
                                { s -> mBinding.desc.text = s }
                        )

                        vaccinationsViewModel.qr.observe(
                                viewLifecycleOwner,
                                { s -> mBinding.imageView2.setImageBitmap(s) }
                        )
                        GlobalScope.launch(Dispatchers.Main) {
                            vaccinationsViewModel.generateQrCode(vaccinationsViewModel.getDataForAdapter(handler)[i])
                        }
                    }
                }
        )
        mBinding.vaccHistory.addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )
    }
}