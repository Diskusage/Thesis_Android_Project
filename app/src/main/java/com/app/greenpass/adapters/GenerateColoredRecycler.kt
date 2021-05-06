package com.app.greenpass.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.greenpass.databinding.ListRowQrBinding
import com.app.greenpass.models.TestModel
import java.lang.ref.WeakReference

//an adapter to demonstrate tests/vaccinations with button option,
//has a callback for click events, colors tests accordingly
class GenerateColoredRecycler(private val dataSet: List<TestModel> = arrayListOf(), private val listener: ClickListener) :
        RecyclerView.Adapter<GenerateColoredRecycler.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(private val binding: ListRowQrBinding, listener: ClickListener) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private val mListenerRef: WeakReference<ClickListener> = WeakReference(listener)
        private val clickListener get() =  mListenerRef.get()

        init {
            // Define click listener for the ViewHolder's View.
            binding.rowButtonQr.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener?.onPositionClicked(adapterPosition)
        }

        fun onBind(dataSet: TestModel) {
            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            binding.textViewResult.text = if (dataSet.testResult) "Positive" else "Negative"

            binding.textViewResult.setTextColor(
                    if (dataSet.testResult) 0xD3E61405.toInt()
                    else 0xBE0F9E27.toInt()
            )
            binding.textViewQr.text = dataSet.toString()
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val inflater = LayoutInflater.from(viewGroup.context)
        return ViewHolder(ListRowQrBinding.inflate(inflater, viewGroup, false), listener)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.onBind(dataSet[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
