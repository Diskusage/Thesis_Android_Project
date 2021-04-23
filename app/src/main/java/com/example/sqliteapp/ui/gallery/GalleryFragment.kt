package com.example.sqliteapp.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.sqliteapp.databinding.FragmentGalleryBinding
import com.example.sqliteapp.models.PersonModel

open class GalleryFragment : Fragment() {
    private val galleryViewModel: GalleryViewModel by activityViewModels()
    private lateinit var mBinding : FragmentGalleryBinding
    protected val binding get() = mBinding
    private var vaccArrayAdapter: ArrayAdapter<*>? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentGalleryBinding.inflate(inflater)


        mBinding.galleryHistory.onItemClickListener = OnItemClickListener { parent, _, position, _ ->
            val clickedPerson = parent.getItemAtPosition(position) as PersonModel

            galleryViewModel.desc.observe(
                    viewLifecycleOwner,
                    { s -> mBinding.desc.text = s}
            )

            galleryViewModel.qr.observe(
                    viewLifecycleOwner,
                    { s -> mBinding.imageView2.setImageBitmap(s) }
            )
            galleryViewModel.generateQrForPopup(clickedPerson)
        }

        galleryViewModel.text.observe(
                viewLifecycleOwner,
                { s -> mBinding.galleryText.text = s }
        )
        showHistory()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        showHistory()
    }

    private fun showHistory(){
        vaccArrayAdapter = ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_list_item_1,
                galleryViewModel.getDataForAdapter()
        )
        mBinding.galleryHistory.adapter = vaccArrayAdapter
    }
}