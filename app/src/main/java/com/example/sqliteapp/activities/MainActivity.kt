package com.example.sqliteapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sqliteapp.database.DatabaseActivity
import com.example.sqliteapp.database.DatabaseHelper
import com.example.sqliteapp.databinding.ActivityMainBinding
import com.example.sqliteapp.models.PersonModel

open class MainActivity : AppCompatActivity() {
    //references to layout controls
    private lateinit var mBinding: ActivityMainBinding
    protected val binding get() = mBinding
    private var databaseHelper: DatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(this.layoutInflater)
        val view = mBinding.root
        setContentView(view)
        databaseHelper = DatabaseHelper(this@MainActivity)


        //button listeners
        binding.btnLogin.setOnClickListener(View.OnClickListener {
            if (
                    binding.etFirstName.text.toString().isEmpty() ||
                    binding.etSecondName.text.toString().isEmpty() ||
                    binding.etIDNP.text.toString().isEmpty()
            ) {
                Toast.makeText(this@MainActivity, "Please fill all the fields.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if (databaseHelper!!.checkLogin(
                            PersonModel(binding.etFirstName.text.toString(),
                                    binding.etSecondName.text.toString(),
                                    binding.etIDNP.text.toString()))
            ) {
                val intent = Intent(this@MainActivity, LoggedInSecond::class.java)
                val toPut = databaseHelper!!.getFromDb(binding.etIDNP.text.toString())
                val backGround = databaseHelper!!.getLastTestResult(binding.etIDNP.text.toString())
                backGround?.let { intent.putExtra("Background", backGround) }
                intent.putExtra("VaccinationType", toPut[0])
                intent.putExtra("VaccinationDate", toPut[1])
                intent.putExtra("IDNP", toPut[2])
                startActivity(intent)
            } else {
                Toast.makeText(this@MainActivity, "No account found", Toast.LENGTH_SHORT).show()
            }
        })
        binding.btnToDB.setOnClickListener {
            val intent = Intent(this@MainActivity, DatabaseActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseHelper = null
    }
}