//package com.app.greenpass.activities.vaccdb
//
//import android.os.Bundle
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.DividerItemDecoration
//import com.app.greenpass.adapters.CustomVaccinationModelAdapter
//import com.app.greenpass.databinding.DatabaseScreenBinding
//import com.app.greenpass.enums.Vaccines
//import com.app.greenpass.models.VaccinationModel
//import com.app.greenpass.util.CoroutineHelper
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//
//class DatabaseVaccinationActivity : AppCompatActivity() {
//    //use binding for more efficient layout control
//    private lateinit var mBinding: DatabaseScreenBinding
//    private val binding get() = mBinding
//    private val handler = CoroutineHelper(this).handler
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val databaseVaccinationViewModel: DatabaseVaccinationViewModel =
//                ViewModelProvider(this)
//                        .get(DatabaseVaccinationViewModel::class.java)
//        databaseVaccinationViewModel.list.observe(this, { s -> showCustomersOnList(s)})
//        GlobalScope.launch(Dispatchers.Main + handler) {
//            databaseVaccinationViewModel.initList(handler)
//        }
//        mBinding = DatabaseScreenBinding.inflate(this.layoutInflater)
//        val view = mBinding.root
//        setContentView(view)
//        //working with database to add a new entry while catching bad entries
//        binding.btnAdd.setOnClickListener {
//            val checkFields = databaseVaccinationViewModel.checkFields(
//                    binding.databaseCode.text.toString(),
//                    binding.series.text.toString(),
//                    binding.lab.text.toString(),
//                    binding.databaseVacc.text.toString(),
//                    binding.databaseDate.text.toString(),
//            )
//            if (checkFields != null){
//                Toast.makeText(this, checkFields, Toast.LENGTH_SHORT).show()
//            }
//            else{
//                if (!databaseVaccinationViewModel.onAdd(
//                                handler,
//                                VaccinationModel(
//                                        Vaccines.getValue(binding.databaseVacc.text.toString()),
//                                        binding.databaseDate.text.toString(),
//                                        binding.lab.text.toString(),
//                                        binding.series.text.toString(),
//                                        binding.databaseCode.text.toString().toInt(),
//                                )
//                        )
//                ){
//                    Toast.makeText(this, "Something went wrong while adding", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//
//    //use recyclerview to demonstrate current entries
//    private fun showCustomersOnList(latestList: MutableList<VaccinationModel>){
//        binding.recyclerView.adapter = CustomVaccinationModelAdapter(
//                latestList,
//                this
//        )
//        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
//
//    }
//}