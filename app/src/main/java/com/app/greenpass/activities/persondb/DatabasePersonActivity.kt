package com.app.greenpass.activities.persondb

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.app.greenpass.adapters.CustomPersonModelAdapter
import com.app.greenpass.databinding.DatabasePersonBinding
import com.app.greenpass.models.PersonModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DatabasePersonActivity: AppCompatActivity() {
    private lateinit var mBinding: DatabasePersonBinding
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
        val databasePersonActivityViewModel: DatabasePersonActivityViewModel =
                ViewModelProvider(this)
                        .get(DatabasePersonActivityViewModel::class.java)
        databasePersonActivityViewModel.list.observe(this, { s -> showPeopleOnList(s) })
        GlobalScope.launch(Dispatchers.Main + handler) {
            databasePersonActivityViewModel.initList(handler)
        }
        mBinding = DatabasePersonBinding.inflate(this.layoutInflater)
        setContentView(mBinding.root)
        binding.btnAdd.setOnClickListener {
            val checkFields = databasePersonActivityViewModel.checkFields(
                    binding.databaseFirstName.text.toString(),
                    binding.databaseSecondName.text.toString(),
                    binding.databaseCode.text.toString(),
            )
            if (checkFields != null){
                Toast.makeText(this, checkFields, Toast.LENGTH_SHORT).show()
            } else{
                if (!databasePersonActivityViewModel.onAdd(
                                handler,
                                PersonModel(
                                        binding.databaseFirstName.text.toString(),
                                        binding.databaseSecondName.text.toString(),
                                        binding.databaseCode.text.toString(),
                                )
                        )
                ){
                    Toast.makeText(this, "Something went wrong while adding", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showPeopleOnList(latestList: MutableList<PersonModel>) {
        binding.recyclerView.adapter = CustomPersonModelAdapter(
                latestList,
                this
        )
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }
}