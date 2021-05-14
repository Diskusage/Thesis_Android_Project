package com.app.greenpass.loginFragments.profile

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.app.greenpass.databinding.FragmentProfileBinding
import kotlinx.coroutines.CoroutineExceptionHandler
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
        mBinding = FragmentProfileBinding.inflate(inflater)

        profileViewModel.persCode.observe(
                viewLifecycleOwner,
                {s: String? -> mBinding.idnpGraph2.text = s }
        )

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
                { s: Int -> mBinding.textTest.text = resources.getString(s) }
        )

        profileViewModel.text.observe(
                viewLifecycleOwner,
                { s: Int -> mBinding.textHome.text = resources.getString(s) }
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

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onResume() {
        super.onResume()
        GlobalScope.launch(Dispatchers.Main) {
            profileViewModel.generateQrCodes(handler)
        }
    }

}