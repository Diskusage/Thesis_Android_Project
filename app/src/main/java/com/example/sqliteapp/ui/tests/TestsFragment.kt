package com.example.sqliteapp.ui.tests

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.sqliteapp.database.DatabaseHelper
import com.example.sqliteapp.databinding.FragmentTestsBinding
import com.example.sqliteapp.activities.LoggedInSecond
import com.example.sqliteapp.models.TestModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder

open class TestsFragment : Fragment() {
    private lateinit var mBinding : FragmentTestsBinding
    protected val binding get() = mBinding
    var idnp: String? = null

    private val testsViewModel: TestsViewModel by activityViewModels()
    private var vaccArrayAdapter: ArrayAdapter<*>? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentTestsBinding.inflate(inflater)

        mBinding.testsHistory.onItemClickListener = OnItemClickListener { parent, _, position, _ ->
            val clickedTest = parent.getItemAtPosition(position) as TestModel

            testsViewModel.desc.observe(
                    viewLifecycleOwner,
                    { s -> mBinding.desc.text = s }
            )

            testsViewModel.qr.observe(
                    viewLifecycleOwner,
                    { s -> mBinding.imageViewTest.setImageBitmap(s) }
            )
            testsViewModel.generateQrCode(clickedTest)
        }

        testsViewModel.text.observe(
                viewLifecycleOwner,
                { s -> mBinding.testsText.text = s }
        )
        showHistory()
        return binding.root

    }


    override fun onResume() {
        super.onResume()
        showHistory()
    }

    private fun showHistory() {
        vaccArrayAdapter = ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_list_item_1,
                testsViewModel.getDataForAdapter()
        )
        mBinding.testsHistory.adapter = vaccArrayAdapter
    }
}