package com.app.greenpass.activities.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
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
    private val count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        val mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        binding.btnLogin.setOnClickListener {
            mainActivityViewModel.getPerson(binding.etIDNP.text.toString().toInt())
            mainActivityViewModel.viewsResult.observe(
                this,
                { s -> when(s){
                    is MainActivityViewModel.ViewResult.Downloaded -> s.person?.let {
                        val intent = Intent(this, LoggedInActivity::class.java)
                        intent.putExtra("user", it.hashCode())
                        startActivity(intent)
                        mainActivityViewModel.viewsResult.removeObservers(this)
                    }
                } }
            )
        }


        //quick login during debugging
        if(BuildConfig.DEBUG){
            binding.etFirstName.text = Editable.Factory.getInstance().newEditable("Lorena")
            binding.etSecondName.text = Editable.Factory.getInstance().newEditable( "Hume")
            binding.etIDNP.text = Editable.Factory.getInstance().newEditable( "1372523338")
        }
    }
//            runBlocking {
//                mainActivityViewModel.onClickedLogin(
//                    listOf(
//                        binding.etFirstName.text.toString(),
//                        binding.etSecondName.text.toString(),
//                        binding.etIDNP.text.toString(),
//                    ),
//                    handler,
//                )?.let {
//                    val intent = Intent(this@MainActivity, LoggedInActivity::class.java)
//                    intent.putExtra("user", .hashCode())
//                    startActivity(intent)
//                } ?: Toast.makeText(this@MainActivity, "No such user", Toast.LENGTH_SHORT).show()
//            }
// go to database population screen
//        binding.btnToDB.setOnClickListener {
//            mainActivityViewModel.populateVaccinations()
//        }
//        binding.btnToTestDb.setOnClickListener{
//            mainActivityViewModel.populateTests()
//        }
//        binding.btnToPeople.setOnClickListener {
//            mainActivityViewModel.populateUsers()
//        }
//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.main_activity, menu)
//        return true
//    }
//
//    override fun onRestart() {
//        super.onRestart()
//        recreate()
//    }



}