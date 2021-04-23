package com.example.sqliteapp.ui.home

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.sqliteapp.databinding.FragmentHomeBinding

open class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var mBinding : FragmentHomeBinding
    protected val binding get() = mBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentHomeBinding.inflate(inflater)

        homeViewModel.firstCode.observe(
                viewLifecycleOwner,
                {s: Bitmap? -> mBinding.qrCode.setImageBitmap(s)}
        )

        homeViewModel.secondCode.observe(
                viewLifecycleOwner,
                {s: Bitmap? -> mBinding.qrCode2.setImageBitmap(s)}
        )

        homeViewModel.text2.observe(
                viewLifecycleOwner,
                { s: String? -> mBinding.textTest.text = s}
        )

        homeViewModel.text.observe(
                viewLifecycleOwner,
                { s: String? -> mBinding.textHome.text = s }
        )
        return binding.root
    }
}