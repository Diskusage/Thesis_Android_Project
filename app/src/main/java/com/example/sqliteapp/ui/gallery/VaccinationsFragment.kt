package com.example.sqliteapp.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.sqliteapp.adapters.ClickListener
import com.example.sqliteapp.adapters.GenerateQrCodeAdapter
import com.example.sqliteapp.databinding.FragmentVaccinationsBinding

open class VaccinationsFragment : Fragment() {
    private val vaccinationsViewModel: VaccinationsViewModel by activityViewModels()
    private lateinit var mBinding : FragmentVaccinationsBinding
    private val binding get() = mBinding

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
                vaccinationsViewModel.getDataForAdapter(),
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
                        vaccinationsViewModel.generateQrCode(vaccinationsViewModel.getDataForAdapter()?.get(i))
                    }
                }
        )
        mBinding.vaccHistory.addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )
    }
}