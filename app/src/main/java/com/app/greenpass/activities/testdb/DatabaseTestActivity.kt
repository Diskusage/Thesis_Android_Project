package com.app.greenpass.activities.testdb

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.app.greenpass.adapters.CustomTestModelAdapter
import com.app.greenpass.databinding.DatabaseTestManagementScreenBinding
import com.app.greenpass.models.TestModel
import kotlinx.coroutines.*

//same thing as DatabaseActivity, but for working with another db table
open class DatabaseTestActivity : AppCompatActivity() {
    //use binding for more efficient layout control
    private lateinit var mBinding: DatabaseTestManagementScreenBinding
    private val binding get() = mBinding
    private val handler = CoroutineExceptionHandler { _, exception ->
        if (Looper.myLooper() == null) {
            Looper.prepare()
            Looper.loop()
        }
        Log.i("Coroutine:", "Error", exception)
        Toast.makeText(this, "Error while adding", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val databaseTestActivityViewModel: DatabaseTestActivityViewModel =
                ViewModelProvider(this)
                .get(DatabaseTestActivityViewModel::class.java)
        databaseTestActivityViewModel.list.observe(this, { s -> showCustomersOnList(s) })
        GlobalScope.launch(Dispatchers.Main + handler) {
            databaseTestActivityViewModel.initList(handler)
        }
        mBinding = DatabaseTestManagementScreenBinding.inflate(this.layoutInflater)
        setContentView(mBinding.root)
        binding.btnTestAdd.setOnClickListener {
            val checkFields = databaseTestActivityViewModel.checkFields(
                    binding.databaseTestIDNP.text.toString(),
                    binding.databaseTestDate.text.toString(),
            )
            if (checkFields != null){
                Toast.makeText(this, checkFields, Toast.LENGTH_SHORT).show()
            } else{
                if (!databaseTestActivityViewModel.onAdd(
                                handler,
                                TestModel(
                                        binding.databaseTestResult.isChecked,
                                        binding.databaseTestDate.text.toString(),
                                        binding.databaseTestAntibodies.isChecked,
                                        binding.databaseTestIDNP.text.toString().toInt()
                                )
                        )
                ){
                    Toast.makeText(this, "Something went wrong while adding", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showCustomersOnList(latestList: MutableList<TestModel>) {
        binding.testRecycler.adapter = CustomTestModelAdapter(
                latestList,
                this
        )
        binding.testRecycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }
}