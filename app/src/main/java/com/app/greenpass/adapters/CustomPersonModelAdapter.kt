//package com.app.greenpass.adapters
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.app.greenpass.databinding.ListRowBinding
//import com.app.greenpass.models.PersonModel
//
//class CustomPersonModelAdapter(private val dataSet: MutableList<PersonModel>, private val con: Context):
//        RecyclerView.Adapter<CustomPersonModelAdapter.ViewHolder>()  {
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
////                    AppDatabase.getInstance(con).DaoPerson().deletePerson(dataSet[position].hashCode())
////                    dataSet.removeAt(position)
////                }
////                launch(Dispatchers.Default) {
////                    AppDatabase.getInstance(con).DaoTest().deleteAllByPerson(dataSet[position].hashCode())
////                }
////                launch(Dispatchers.Default) {
////                    AppDatabase.getInstance(con).DaoVaccinations().deleteFromPerson(dataSet[position].hashCode())
////                }
////                db().join()
////                notifyDataSetChanged()
////            }
////        }
//    }
//
//    // Return the size of your dataset (invoked by the layout manager)
//    override fun getItemCount() = dataSet.size
//}