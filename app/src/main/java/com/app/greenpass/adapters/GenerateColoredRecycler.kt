package com.app.greenpass.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.app.greenpass.R
import com.app.greenpass.databinding.ListRowQrBinding
import com.app.greenpass.loginFragments.viewmodels.TestsViewModel
import com.app.greenpass.models.TestModel
import kotlinx.coroutines.launch


//an adapter to demonstrate tests/vaccinations with button option,
//has a callback for click events, colors tests accordingly
class GenerateColoredRecycler(private val dataSet: List<TestModel> = arrayListOf(), private val testsViewModel: TestsViewModel) :
        RecyclerView.Adapter<GenerateColoredRecycler.ViewHolder>()
    {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(private val binding: ListRowQrBinding, private val testsViewModel: TestsViewModel, private val list: List<TestModel>)
        : RecyclerView.ViewHolder(binding.root) {

        fun onBind(dataSet: TestModel) {
            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            if (dataSet.testResult){
                binding.textViewResult.text = itemView.resources.getText(R.string.test_pos)
                binding.textViewResult.setTextColor(0xD3E61405.toInt())
            } else {
                binding.textViewResult.text = itemView.resources.getText(R.string.test_neg)
                binding.textViewResult.setTextColor(0xBE0F9E27.toInt())
            }
            binding.rowButtonQr.setOnClickListener {
                testsViewModel.viewModelScope.launch {
                    testsViewModel.generateQrCode(list[adapterPosition])
                }
            }
            binding.textViewQr.text = dataSet.display()
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val inflater = LayoutInflater.from(viewGroup.context)
        return ViewHolder(ListRowQrBinding.inflate(inflater, viewGroup, false), testsViewModel, dataSet)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.onBind(dataSet[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}
