package com.app.greenpass.activities.main

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.app.greenpass.BuildConfig
import com.app.greenpass.R
import com.app.greenpass.activities.locale.BaseActivity
import com.app.greenpass.activities.login.LoggedInActivity
import com.app.greenpass.activities.persondb.DatabasePersonActivity
import com.app.greenpass.activities.testdb.DatabaseTestActivity
import com.app.greenpass.activities.vaccdb.DatabaseVaccinationActivity
import com.app.greenpass.databinding.ActivityMainBinding
import kotlinx.coroutines.*


//the first activity you see upon starting the app,
open class MainActivity : BaseActivity() {
    //use binding for more efficient layout control
    private lateinit var mBinding: ActivityMainBinding
    private val binding get() = mBinding
    private val handler = CoroutineExceptionHandler{_, exception ->
        if (Looper.myLooper() == null) {
            Looper.prepare()
            Looper.loop()
        }
        Log.i("Coroutine:", "Error", exception)
        Toast.makeText(this, "Error: ${exception.cause}", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(this.layoutInflater)
        setContentView(mBinding.root)
        val mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        binding.btnLogin.setOnClickListener {
            runBlocking {
                val checkLogin = mainActivityViewModel.onClickedLogin(
                        binding.etFirstName.text.toString(),
                        binding.etSecondName.text.toString(),
                        binding.etIDNP.text.toString(),
                        handler
                )
                if (checkLogin != null){
                    val intent = Intent(this@MainActivity, LoggedInActivity::class.java)
                    intent.putExtra("user", checkLogin.hashCode())
                    startActivity(intent)
                } else {
                    Toast.makeText(this@MainActivity, "No such user", Toast.LENGTH_SHORT).show()
                }
            }
        }
        //go to database population screen
        binding.btnToDB.setOnClickListener {
            val intent = Intent(this@MainActivity, DatabaseVaccinationActivity::class.java)
            startActivity(intent)
        }
        binding.btnToTestDb.setOnClickListener{
            val intent = Intent(this@MainActivity, DatabaseTestActivity::class.java)
            startActivity(intent)
        }

        binding.btnToPeople.setOnClickListener {
            val intent = Intent(this@MainActivity, DatabasePersonActivity::class.java)
            startActivity(intent)
        }


        if(BuildConfig.DEBUG){
            binding.etFirstName.text = Editable.Factory.getInstance().newEditable("d")
            binding.etSecondName.text = Editable.Factory.getInstance().newEditable( "b")
            binding.etIDNP.text =Editable.Factory.getInstance().newEditable( "1234567890122")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity, menu)
        return true
    }
    //TODO language in main menu
    //TODO database data translations where possible
    override fun onRestart() {
        super.onRestart()
        recreate()
    }

}