package com.example.sqliteapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sqliteapp.R
import com.example.sqliteapp.models.TestModel
import org.w3c.dom.Text
import java.lang.ref.WeakReference

//an adapter to demonstrate tests/vaccinations with button option,
//has a callback for click events, colors tests accordingly
class GenerateColoredRecycler(private val dataSet: MutableList<TestModel>?, private val listener: ClickListener) :
        RecyclerView.Adapter<GenerateColoredRecycler.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View, listener: ClickListener) : RecyclerView.ViewHolder(view), View.OnClickListener{

        val result: TextView = view.findViewById(R.id.textViewResult)
        val person: TextView = view.findViewById(R.id.textViewQr)
        private val btn: Button = view.findViewById(R.id.rowButtonQr)
        private val listenerRef: WeakReference<ClickListener> = WeakReference(listener)

        init {
            // Define click listener for the ViewHolder's View.
            btn.setOnClickListener(this)
        }

        override fun onClick(v: View?){
            listenerRef.get()?.onPositionClicked(adapterPosition)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.list_row_qr, viewGroup, false)

        return ViewHolder(view, listener)
    }
//D3E61405
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.result.text = if (dataSet?.get(position)?.testResult == true){
            "Positive"
        } else "Negative"
        viewHolder.result.setTextColor(
                if (dataSet?.get(position)?.testResult == true) 0xD3E61405.toInt()
                else 0xBE0F9E27.toInt()
        )
        viewHolder.person.text = dataSet?.get(position)?.toString() ?: return
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet?.size ?: -1

}
