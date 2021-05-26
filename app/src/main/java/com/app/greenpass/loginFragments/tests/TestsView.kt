package com.app.greenpass.loginFragments.tests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.app.greenpass.adapters.GenerateColoredRecycler
import com.app.greenpass.databinding.FragmentTestsBinding

//MVVM architecture for fragments
//a tests view, consists of layout bindings and
//listeners for user interaction
open class TestsView : Fragment() {
    private lateinit var mBinding : FragmentTestsBinding
    private val binding get() = mBinding
    var idnp: String? = null
    private val testsViewModel: TestsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentTestsBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        testsViewModel.viewsResult.observe(
            viewLifecycleOwner,
            { s -> when(s){
                is TestsViewModel.ViewResult.Opened -> s.apply {
                    binding.testsText.text = resources.getString(text)
                    binding.testsHistory.adapter = GenerateColoredRecycler(list, testsViewModel)
                    binding.testsHistory.addItemDecoration(
                        DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
                    )
                }
                is TestsViewModel.ViewResult.Generated -> s.apply {
                    binding.desc.text = resources.getString(testText)
                    binding.imageViewTest.setImageBitmap(testMap)
                }
            } }
        )
    }

    override fun onResume() {
        super.onResume()
        testsViewModel.updateView()
        binding.desc.text = null
        binding.imageViewTest.setImageBitmap(null)
    }

}