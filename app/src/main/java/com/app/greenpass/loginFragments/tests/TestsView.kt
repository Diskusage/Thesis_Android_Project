package com.app.greenpass.loginFragments.tests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.app.greenpass.adapters.ClickListener
import com.app.greenpass.adapters.GenerateColoredRecycler
import com.app.greenpass.databinding.FragmentTestsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

        testsViewModel.text.observe(
                viewLifecycleOwner,
                { s -> mBinding.testsText.text = s }
        )
        return binding.root

    }


    override fun onResume() {
        super.onResume()
        showList()
    }

    private fun showList(){

        mBinding.testsHistory.adapter = GenerateColoredRecycler(
                testsViewModel.getDataForAdapter(),
                object : ClickListener {
                    override fun onPositionClicked(i: Int) {
                        testsViewModel.desc.observe(
                                viewLifecycleOwner,
                                { s -> mBinding.desc.text = s }
                        )

                        testsViewModel.qr.observe(
                                viewLifecycleOwner,
                                { s -> mBinding.imageViewTest.setImageBitmap(s) }
                        )
                        GlobalScope.launch(Dispatchers.Main) {
                            testsViewModel.generateQrCode(testsViewModel.getDataForAdapter()[i])
                        }
                    }
                }
        )
        mBinding.testsHistory.addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )
    }

}