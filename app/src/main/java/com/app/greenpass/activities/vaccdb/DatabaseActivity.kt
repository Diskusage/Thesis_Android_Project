package com.app.greenpass.activities.vaccdb

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.app.greenpass.adapters.CustomPersonModelAdapter
import com.app.greenpass.databinding.DatabaseScreenBinding
import com.app.greenpass.enums.Vaccines
import com.app.greenpass.models.PersonModel
import kotlinx.coroutines.*
import java.security.InvalidKeyException
import java.util.*

open class DatabaseActivity : AppCompatActivity() {
    //use binding for more efficient layout control
    private lateinit var mBinding: DatabaseScreenBinding
    private val binding get() = mBinding
    private val handler = CoroutineExceptionHandler { _, exception ->
        if (Looper.myLooper() == null){
            Looper.prepare()
            Looper.loop()
        }
        Log.i("Coroutine:", "Error", exception)
        Toast.makeText(this, "Error while adding", Toast.LENGTH_SHORT).show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val databaseActivityViewModel: DatabaseActivityViewModel =
                ViewModelProvider(this)
                        .get(DatabaseActivityViewModel::class.java)
        databaseActivityViewModel.list.observe(this, {s -> showCustomersOnList(s)})
        GlobalScope.launch(Dispatchers.Main + handler) {
            databaseActivityViewModel.initList(handler)
        }
        mBinding = DatabaseScreenBinding.inflate(this.layoutInflater)
        val view = mBinding.root
        setContentView(view)
        //working with database to add a new entry while catching bad entries
        binding.btnAdd.setOnClickListener {
            try {
                databaseActivityViewModel.checkFields(
                        binding.databaseFirstName.text.toString(),
                        binding.databaseSecondName.text.toString(),
                        binding.databaseIDNP.text.toString(),
                        binding.databaseVacc.text.toString(),
                        binding.databaseDate.text.toString()
                )
                try {
                    databaseActivityViewModel.onAdd(
                            handler,
                            PersonModel(
                                    binding.databaseFirstName.text.toString(),
                                    binding.databaseSecondName.text.toString(),
                                    binding.databaseIDNP.text.toString(),
                                    Vaccines.valueOf(binding.databaseVacc.text.toString()),
                                    binding.databaseDate.text.toString()
                            )
                    )
                } catch (exception: Exception) {
                    Toast.makeText(
                            this@DatabaseActivity,
                            "Error creating a person with this data",
                            Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (exception: IllegalArgumentException) {
                Toast.makeText(
                        this@DatabaseActivity,
                        "Please fill all the fields.",
                        Toast.LENGTH_SHORT
                ).show()
            } catch (exception: InvalidKeyException) {
                Toast.makeText(
                        this@DatabaseActivity,
                        "IDNP should have 13 characters",
                        Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    //use recyclerview to demonstrate current entries
    private fun showCustomersOnList(latestList: MutableList<PersonModel>){
        binding.recyclerView.adapter = CustomPersonModelAdapter(
                latestList,
                this
        )
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

    }
}