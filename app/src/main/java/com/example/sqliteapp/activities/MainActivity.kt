package com.example.sqliteapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sqliteapp.database.DatabaseActivity
import com.example.sqliteapp.database.DatabaseHelper
import com.example.sqliteapp.database.DatabaseTestActivity
import com.example.sqliteapp.databinding.ActivityMainBinding
import com.example.sqliteapp.models.PersonModel


//the first activity you see upon starting the app,
open class MainActivity : AppCompatActivity() {
    //use binding for more efficient layout control
    private lateinit var mBinding: ActivityMainBinding
    private val binding get() = mBinding
    //create a db helper to work with database
    private var databaseHelper: DatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(this.layoutInflater)
        val view = mBinding.root
        setContentView(view)
        databaseHelper = DatabaseHelper(this@MainActivity)


        //Login button event - checks all necessary things - whether the user has a profile,
        //and transfers important data for later use
        binding.btnLogin.setOnClickListener(View.OnClickListener {
            if (
                    binding.etFirstName.text.toString().isEmpty() ||
                    binding.etSecondName.text.toString().isEmpty() ||
                    binding.etIDNP.text.toString().isEmpty()
            ) {
                Toast.makeText(this@MainActivity, "Please fill all the fields.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if (databaseHelper?.checkLogin(
                            PersonModel(binding.etFirstName.text.toString(),
                                    binding.etSecondName.text.toString(),
                                    binding.etIDNP.text.toString())) == true
            ) {
                val intent = Intent(this@MainActivity, LoggedInActivity::class.java)
                val backGround = databaseHelper?.getLastTestResult(binding.etIDNP.text.toString())
                val toPut = databaseHelper?.getFromDb(mBinding.etIDNP.text.toString())
                if (toPut == null && backGround != null) {
                    val toPutTest = databaseHelper!!.getTestFromDb(mBinding.etIDNP.text.toString())
                    intent.putExtra("Background", backGround)
                    intent.putExtra("IDNP", toPutTest?.idnp)
                    intent.putExtra("FName", binding.etFirstName.text.toString())
                    intent.putExtra("SName", binding.etSecondName.text.toString())
                } else if (toPut != null && backGround == null) {
                    intent.putExtra("VaccinationType", toPut.type.toString())
                    intent.putExtra("VaccinationDate", toPut.vaccDate)
                    intent.putExtra("IDNP", toPut.iDNP)
                    intent.putExtra("FName", toPut.firstName)
                    intent.putExtra("SName", toPut.secondName)
                } else if (toPut != null && backGround != null) {
                    intent.putExtra("VaccinationType", toPut.type)
                    intent.putExtra("VaccinationDate", toPut.vaccDate)
                    intent.putExtra("IDNP", toPut.iDNP)
                    intent.putExtra("FName", toPut.firstName)
                    intent.putExtra("SName", toPut.secondName)
                    intent.putExtra("Background", backGround)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this@MainActivity, "No account found", Toast.LENGTH_SHORT).show()
            }
        })
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
//prevent memory leaks
    override fun onDestroy() {
        super.onDestroy()
        databaseHelper = null
    }


}