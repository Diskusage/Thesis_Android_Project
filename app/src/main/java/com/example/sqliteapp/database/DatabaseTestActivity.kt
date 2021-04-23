package com.example.sqliteapp.database

import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import com.example.sqliteapp.databinding.DatabaseTestManagementScreenBinding
import com.example.sqliteapp.models.TestModel

open class DatabaseTestActivity : AppCompatActivity() {
    private lateinit var mBinding: DatabaseTestManagementScreenBinding
    protected val binding get() = mBinding
    private val idnpLength = 13
    private var personArrayAdapter: ArrayAdapter<*>? = null
    private var databaseHelper: DatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DatabaseTestManagementScreenBinding.inflate(this.layoutInflater)
        val view = mBinding.root
        setContentView(view)
        databaseHelper = DatabaseHelper(this@DatabaseTestActivity)
        showCustomersOnList(databaseHelper!!)
        binding.dbTestBack.setOnClickListener { finish() }
        binding.btnTestAdd.setOnClickListener(View.OnClickListener {
            if (!checkFields()) return@OnClickListener
            val testModel: TestModel?
            try {
                testModel = TestModel(
                        binding.databaseTestIDNP.text.toString(),
                        binding.databaseTestResult.isChecked,
                        binding.databaseTestDate.text.toString(),
                        binding.databaseTestAntibodies.isChecked
                )
            } catch (e: Exception) {
                Toast.makeText(
                        this@DatabaseTestActivity,
                        "Error adding a person",
                        Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            val databaseHelper = DatabaseHelper(this@DatabaseTestActivity)
            val success: Boolean = databaseHelper.addTest(testModel)
            Toast.makeText(
                    this@DatabaseTestActivity,
                    "Success= $success",
                    Toast.LENGTH_SHORT
            ).show()
            showCustomersOnList(databaseHelper)
        })
        binding.databaseListView.onItemClickListener = OnItemClickListener { parent, _, position, _ ->
            val clickedPerson = parent.getItemAtPosition(position) as TestModel
            databaseHelper!!.deleteTest(clickedPerson)
            showCustomersOnList(databaseHelper!!)
            Toast.makeText(
                    this@DatabaseTestActivity,
                    "Deleted $clickedPerson",
                    Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showCustomersOnList(databaseHelper2: DatabaseHelper) {
        personArrayAdapter = ArrayAdapter(
                this@DatabaseTestActivity,
                android.R.layout.simple_list_item_1,
                databaseHelper2.allTests
        )
        binding.databaseListView.adapter = personArrayAdapter
    }

    private fun checkFields(): Boolean {
        if (binding.databaseTestIDNP.length() == 0 || binding.databaseTestDate.length() == 0) {
            Toast.makeText(
                    this@DatabaseTestActivity,
                    "Please fill all the fields.",
                    Toast.LENGTH_SHORT
            ).show()
            return false
        }
        if (binding.databaseTestIDNP.length() != idnpLength) {
            Toast.makeText(
                    this@DatabaseTestActivity,
                    "IDNP should have 13 characters",
                    Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseHelper = null
    }
}