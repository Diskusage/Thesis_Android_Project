package com.example.sqliteapp.database

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.sqliteapp.adapters.CustomTestModelAdapter
import com.example.sqliteapp.databinding.DatabaseTestManagementScreenBinding
import com.example.sqliteapp.models.TestModel
//same thing as DatabaseActivity, but for working with another db table
open class DatabaseTestActivity : AppCompatActivity() {
    //use binding for more efficient layout control
    private lateinit var mBinding: DatabaseTestManagementScreenBinding
    private val binding get() = mBinding
    //idnp is 13 numbers long
    private val idnpLength = 13
    //helper to work with db
    private var databaseHelper: DatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DatabaseTestManagementScreenBinding.inflate(this.layoutInflater)
        val view = mBinding.root
        setContentView(view)
        databaseHelper = DatabaseHelper(this@DatabaseTestActivity)
        showCustomersOnList(databaseHelper!!)
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

    }

    private fun showCustomersOnList(databaseHelper2: DatabaseHelper) {
        binding.testRecycler.adapter = CustomTestModelAdapter(databaseHelper2.allTests, databaseHelper2)
        binding.testRecycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
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