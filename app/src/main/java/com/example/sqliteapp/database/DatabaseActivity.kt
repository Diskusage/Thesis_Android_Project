package com.example.sqliteapp.database

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.sqliteapp.adapters.CustomPersonModelAdapter
import com.example.sqliteapp.databinding.DatabaseScreenBinding
import com.example.sqliteapp.exceptions.EmptyRecordAfterExistingVaccinationException
import com.example.sqliteapp.exceptions.MatchingVaccinationException
import com.example.sqliteapp.exceptions.VaccineDoesntExist
import com.example.sqliteapp.models.PersonModel
import java.util.*

open class DatabaseActivity : AppCompatActivity() {
    //references to layout controls
    private lateinit var mBinding: DatabaseScreenBinding
    private val binding get() = mBinding
    private val idnpLength = 13
    private var databaseHelper: DatabaseHelper? = null
    private var dbDateListener: OnDateSetListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DatabaseScreenBinding.inflate(this.layoutInflater)
        val view = mBinding.root
        setContentView(view)
        databaseHelper = DatabaseHelper(this@DatabaseActivity)
        binding.dbBack.setOnClickListener {
            val intent = Intent(this@DatabaseActivity, DatabaseTestActivity::class.java)
            startActivity(intent)
        }
        binding.btnAdd.setOnClickListener {
            var isDateSet = false
            if (binding.dateId.text != "Date of vaccination") isDateSet = true
            if (!checkFields()) return@setOnClickListener
            var personModel: PersonModel? = null
            try {
                when {
                    binding.databaseVacc.length() == 0 -> personModel = PersonModel(
                            binding.databaseFirstName.text.toString(),
                            binding.databaseSecondName.text.toString(),
                            binding.databaseIDNP.text.toString()
                    )
                    isDateSet -> personModel = PersonModel(
                            binding.databaseFirstName.text.toString(),
                            binding.databaseSecondName.text.toString(),
                            binding.databaseIDNP.text.toString(),
                            binding.databaseVacc.text.toString(),
                            binding.dateId.text.toString())
                    else -> Toast.makeText(
                            this@DatabaseActivity,
                            "Choose the date",
                            Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: VaccineDoesntExist) {
                Toast.makeText(
                        this@DatabaseActivity,
                        "Enter correct vaccine name",
                        Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Toast.makeText(
                        this@DatabaseActivity,
                        "Error adding a person",
                        Toast.LENGTH_SHORT
                ).show()
            }
            if (personModel == null){ return@setOnClickListener }
            val databaseHelper = DatabaseHelper(this@DatabaseActivity)
            var success = false
            try {
                success = databaseHelper.addOne(personModel)
            }
            catch (e: MatchingVaccinationException) {
                Toast.makeText(
                        this@DatabaseActivity,
                        "This person was already vaccinated on that date",
                        Toast.LENGTH_SHORT
                ).show()
            }
            catch (e: EmptyRecordAfterExistingVaccinationException) {
                Toast.makeText(
                        this@DatabaseActivity,
                        "Can't add an empty record if the person was vaccinated in the past",
                        Toast.LENGTH_SHORT
                ).show()
            }
            Toast.makeText(
                    this@DatabaseActivity,
                    "Success= $success",
                    Toast.LENGTH_SHORT
            ).show()
            showCustomersOnList(databaseHelper)
        }
        binding.dateId.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal[Calendar.YEAR]
            val month = cal[Calendar.MONTH]
            val day = cal[Calendar.DAY_OF_MONTH]
            val dialog = DatePickerDialog(
                    this@DatabaseActivity,
                    android.R.style.Theme_Black,
                    dbDateListener,
                    year,
                    month,
                    day
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
        dbDateListener = OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
            val realMonth = month+1
            val date = "$dayOfMonth/$realMonth/$year"
            binding.dateId.text = date
        }
    }

    private fun showCustomersOnList(databaseHelper2: DatabaseHelper) {
        binding.recyclerView.adapter = CustomPersonModelAdapter(databaseHelper2.all, databaseHelper2)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun checkFields(): Boolean {
        if (
                binding.databaseIDNP.length() == 0 ||
                binding.databaseFirstName.length() == 0 ||
                binding.databaseSecondName.length() == 0
        ) {
            Toast.makeText(
                    this@DatabaseActivity,
                    "Please fill all the fields.",
                    Toast.LENGTH_SHORT
            ).show()
            return false
        }
        if (binding.databaseIDNP.length() != idnpLength) {
            Toast.makeText(
                    this@DatabaseActivity,
                    "IDNP should have 13 characters",
                    Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        showCustomersOnList(databaseHelper!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseHelper = null
    }
}