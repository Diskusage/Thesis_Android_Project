package com.app.greenpass.loginFragments.vaccinations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.app.greenpass.adapters.ClickListener
import com.app.greenpass.adapters.GenerateQrCodeAdapter
import com.app.greenpass.databinding.FragmentVaccinationsBinding
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

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentVaccinationsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vaccinationsViewModel.viewsResult.observe(
            viewLifecycleOwner,
            { s -> when(s){
                is VaccinationsViewModel.ViewResult.Opened -> s.apply {
                    binding.galleryText.text = resources.getString(text)
                    binding.vaccHistory.adapter = GenerateQrCodeAdapter(
                        list,
                        object : ClickListener {
                            override fun onPositionClicked(i: Int) {
                                GlobalScope.launch(Dispatchers.Main) {
                                    vaccinationsViewModel.generateQrCode(list[i])
                                }
                            }
                        }
                    )
                    binding.vaccHistory.addItemDecoration(
                        DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
                    )
                }
                is VaccinationsViewModel.ViewResult.Generated -> s.apply {
                    binding.desc.text = resources.getString(testText)
                    binding.imageView2.setImageBitmap(testMap)
                }
            } }
        )
    }

    override fun onResume() {
        super.onResume()
        vaccinationsViewModel.updateView()
        binding.desc.text = null
        binding.imageView2.setImageBitmap(null)
    }

}