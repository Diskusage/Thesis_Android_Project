package com.example.sqliteapp.activities.main

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sqliteapp.activities.vaccdb.DatabaseActivity
import com.example.sqliteapp.activities.testdb.DatabaseTestActivity
import com.example.sqliteapp.activities.login.LoggedInActivity
import com.example.sqliteapp.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.lang.IllegalArgumentException


//the first activity you see upon starting the app,
open class MainActivity : AppCompatActivity() {
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
        val view = mBinding.root
        setContentView(view)
        val mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        binding.btnLogin.setOnClickListener {
            try {
                mainActivityViewModel.onClickedLogin(
                        binding.etFirstName.text.toString(),
                        binding.etSecondName.text.toString(),
                        binding.etIDNP.text.toString(),
                        handler
                )
                val intent = Intent(this@MainActivity, LoggedInActivity::class.java)
                intent.putExtra("fn", binding.etFirstName.text.toString())
                intent.putExtra("IDNP", binding.etIDNP.text.toString())
                intent.putExtra("sn", binding.etSecondName.text.toString())
                startActivity(intent)
            } catch (exception: IllegalArgumentException){
                Toast.makeText(this@MainActivity, "Please fill all the fields.", Toast.LENGTH_SHORT).show()
            } catch (exception: NoSuchElementException){
                Toast.makeText(this@MainActivity, "No account found", Toast.LENGTH_SHORT).show()
            }
        }
        //go to database population screen
        binding.btnToDB.setOnClickListener {
            val intent = Intent(this@MainActivity, DatabaseActivity::class.java)
            startActivity(intent)
        }
        binding.btnToTestDb.setOnClickListener{
            val intent = Intent(this@MainActivity, DatabaseTestActivity::class.java)
            startActivity(intent)
        }
    }
}