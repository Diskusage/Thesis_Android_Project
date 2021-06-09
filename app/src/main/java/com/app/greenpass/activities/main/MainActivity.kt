package com.app.greenpass.activities.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.app.greenpass.BuildConfig
import com.app.greenpass.activities.login.LoggedInActivity
import com.app.greenpass.databinding.ActivityMainBinding


//the first activity you see upon starting the app,
class MainActivity : AppCompatActivity() {
    //use binding for more efficient layout control
    private lateinit var mBinding: ActivityMainBinding
    private val binding get() = mBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        val mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        binding.btnLogin.setOnClickListener {
            mainActivityViewModel.getPerson(
                binding.etIDNP.text.toString().toInt(),
                binding.etFirstName.text.toString(),
                binding.etSecondName.text.toString()
            )
        }

        mainActivityViewModel.viewsResult.observe(
            this,
            { s ->
                when (s) {
                    is MainActivityViewModel.ViewResult.Downloaded -> s.person?.let {
                        val intent = Intent(this, LoggedInActivity::class.java)
                        intent.putExtra("user", it.hashCode())
                        startActivity(intent)
                    }
                    is MainActivityViewModel.ViewResult.NotFound -> {
                        val data = resources.getString(s.text)
                        Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )

        //quick login during debugging
        if (BuildConfig.DEBUG) {
            binding.etFirstName.text = Editable.Factory.getInstance().newEditable("Lorena")
            binding.etSecondName.text = Editable.Factory.getInstance().newEditable("Hume")
            binding.etIDNP.text = Editable.Factory.getInstance().newEditable("1372523338")
        }
    }
}