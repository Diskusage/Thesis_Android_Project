package com.example.sqliteapp.ui.home

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.sqliteapp.databinding.FragmentProfileBinding

open class ProfileFragment : Fragment() {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var mBinding : FragmentProfileBinding
    private val binding get() = mBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentProfileBinding.inflate(inflater)

        profileViewModel.firstCode.observe(
                viewLifecycleOwner,
                {s: Bitmap? -> mBinding.qrCode.setImageBitmap(s)}
        )

        profileViewModel.secondCode.observe(
                viewLifecycleOwner,
                {s: Bitmap? -> mBinding.qrCode2.setImageBitmap(s)}
        )

        profileViewModel.text2.observe(
                viewLifecycleOwner,
                { s: String? -> mBinding.textTest.text = s }
        )

        profileViewModel.text.observe(
                viewLifecycleOwner,
                { s: String? -> mBinding.textHome.text = s }
        )

        profileViewModel.fNameGraph.observe(
                viewLifecycleOwner,
                { s: String? -> mBinding.firstNameGraph.text = s }
        )

        profileViewModel.sNameGraph.observe(
                viewLifecycleOwner,
                { s: String? -> mBinding.secondNameGraph.text = s }
        )

        profileViewModel.iDnpGraph.observe(
                viewLifecycleOwner,
                { s: String? -> mBinding.idnpGraph.text = s }
        )
        return binding.root
    }
}