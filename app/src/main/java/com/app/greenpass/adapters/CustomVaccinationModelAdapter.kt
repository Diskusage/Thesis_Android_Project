//package com.app.greenpass.adapters
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.app.greenpass.databinding.ListRowBinding
//import com.app.greenpass.models.VaccinationModel
//
////adapter to demonstrate all vaccination entries in the database with RecyclerView list
//class CustomVaccinationModelAdapter(private val dataSet: MutableList<VaccinationModel>, private val con: Context) :
//        RecyclerView.Adapter<CustomVaccinationModelAdapter.ViewHolder>() {
//    /**
//     * Provide a reference to the type of views that you are using
//     * (custom ViewHolder).
//     */
//    class ViewHolder(binding: ListRowBinding) : RecyclerView.ViewHolder(binding.root) {
//        val person: TextView = binding.textView
////        val btn: Button = binding.rowButton
//    }
//
//    // Create new views (invoked by the layout manager)
//    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
//        // Create a new view, which defines the UI of the list item
//        val inflater = LayoutInflater.from(viewGroup.context)
//        return ViewHolder(ListRowBinding.inflate(inflater, viewGroup, false))
//    }
//
//    // Replace the contents of a view (invoked by the layout manager)
//    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
//        // Get element from your dataset at this position and replace the
//        // contents of the view with that element
//
//        viewHolder.person.text = dataSet[position].toString()
////        viewHolder.btn.setOnClickListener {
////            runBlocking(handler) {
////                fun db() = launch {
////                    val dif = AppDatabase.getInstance(con).DaoVaccinations().getIdForDeletion(dataSet[position].owner)[0]
////                    AppDatabase.getInstance(con).DaoVaccinations().deletePerson(position+dif)
////                    dataSet.removeAt(position)
////                }
////                db().join()
////                notifyDataSetChanged()
////            }
////        }
//    }
//
//    // Return the size of your dataset (invoked by the layout manager)
//    override fun getItemCount() = dataSet.size
//
//}
