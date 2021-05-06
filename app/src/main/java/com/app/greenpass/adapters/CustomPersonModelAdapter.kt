package com.app.greenpass.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.greenpass.database.AppDatabase
import com.app.greenpass.databinding.ListRowBinding
import com.app.greenpass.models.PersonModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

//adapter to demonstrate all vaccination entries in the database with RecyclerView list
class CustomPersonModelAdapter(private val dataSet: MutableList<PersonModel>, private val con: Context) :
        RecyclerView.Adapter<CustomPersonModelAdapter.ViewHolder>() {
    private val handler = CoroutineExceptionHandler{ _, exception ->
        Toast.makeText(con, "Person adapter coroutine error: $exception", Toast.LENGTH_SHORT).show()
    }
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(binding: ListRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val person: TextView = binding.textView
        val btn: Button = binding.rowButton
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val inflater = LayoutInflater.from(viewGroup.context)
        return ViewHolder(ListRowBinding.inflate(inflater, viewGroup, false))
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        viewHolder.person.text = dataSet[position].toString()
        viewHolder.btn.setOnClickListener {
            runBlocking(handler) {
                fun db() = launch {
                    AppDatabase.getInstance(con).DaoPerson().deletePerson(dataSet[position].toCode())
                    dataSet.removeAt(position)
                }
                db().join()
                notifyDataSetChanged()
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
