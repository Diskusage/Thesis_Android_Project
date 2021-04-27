package com.example.sqliteapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sqliteapp.R
import java.lang.ref.WeakReference

class GenerateQrCodeAdapter(private val dataSet: MutableList<*>?, private val listener: ClickListener) :
        RecyclerView.Adapter<GenerateQrCodeAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View, listener: ClickListener) : RecyclerView.ViewHolder(view), View.OnClickListener{

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

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.person.text = dataSet?.get(position)?.toString() ?: return
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet?.size ?: -1

}
