package com.app.greenpass.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.greenpass.databinding.ListRowQrBinding
import com.app.greenpass.loginFragments.vaccinations.VaccinationsViewModel
import com.app.greenpass.models.VaccinationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//an adapter to demonstrate tests/vaccinations with button option,
//has a callback for click events
class GenerateQrCodeAdapter(private val dataSet: List<VaccinationModel> = arrayListOf(), private val vaccinationsViewModel: VaccinationsViewModel) :
        RecyclerView.Adapter<GenerateQrCodeAdapter.ViewHolder>()  {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(private val binding: ListRowQrBinding,
                     private val vaccinationsViewModel: VaccinationsViewModel,
                     private val list: List<VaccinationModel>)
        : RecyclerView.ViewHolder(binding.root){

        fun onBind(dataSet: VaccinationModel){
            binding.textViewQr.text = dataSet.toString()
            binding.rowButtonQr.setOnClickListener {
                GlobalScope.launch(Dispatchers.Default) {
                    vaccinationsViewModel.generateQrCode(list[adapterPosition])
                }
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val inflater = LayoutInflater.from(viewGroup.context)
        return ViewHolder(ListRowQrBinding.inflate(inflater, viewGroup, false), vaccinationsViewModel, dataSet)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.onBind(dataSet[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
