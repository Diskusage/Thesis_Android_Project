package com.example.sqliteapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sqliteapp.R
import com.example.sqliteapp.database.DatabaseHelper
import com.example.sqliteapp.models.PersonModel

//adapter to demonstrate all vaccination entries in the database with RecyclerView list
class CustomPersonModelAdapter(private val dataSet: MutableList<PersonModel>, private val databaseHelper: DatabaseHelper) :
        RecyclerView.Adapter<CustomPersonModelAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val person: TextView = view.findViewById(R.id.textView)
        val btn: Button = view.findViewById(R.id.rowButton)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.list_row, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.person.text = dataSet[position].toString()
        viewHolder.btn.setOnClickListener {
            databaseHelper.deleteOne(dataSet[position])
            dataSet.removeAt(position)
            notifyDataSetChanged()
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
