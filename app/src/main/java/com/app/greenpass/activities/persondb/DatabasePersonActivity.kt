//package com.app.greenpass.activities.persondb
//
//import android.os.Bundle
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.DividerItemDecoration
//import com.app.greenpass.adapters.CustomPersonModelAdapter
//import com.app.greenpass.databinding.DatabasePersonBinding
//import com.app.greenpass.models.PersonModel
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//
//class DatabasePersonActivity: AppCompatActivity() {
//    private lateinit var mBinding: DatabasePersonBinding
//    private val binding get() = mBinding
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val databasePersonActivityViewModel: DatabasePersonActivityViewModel =
//                ViewModelProvider(this)
//                        .get(DatabasePersonActivityViewModel::class.java)
//        databasePersonActivityViewModel.list.observe(this, { s -> showPeopleOnList(s) })
//        GlobalScope.launch(Dispatchers.IO) {
//            databasePersonActivityViewModel.initList()
//        }
//        mBinding = DatabasePersonBinding.inflate(layoutInflater)
//        setContentView(mBinding.root)
//
//        binding.btnAdd.setOnClickListener {
//            databasePersonActivityViewModel.addPerson(
//                binding.databaseFirstName.text.toString(),
//                binding.databaseSecondName.text.toString(),
//                binding.databaseCode.text.toString(),
//            ).second?.let {
//                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun showPeopleOnList(latestList: MutableList<PersonModel>) {
//        binding.recyclerView.adapter = CustomPersonModelAdapter(
//                latestList,
//                this
//        )
//        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
//    }
//}