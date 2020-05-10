package com.example.inoapp.yourtrips

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.inoapp.R
import com.example.inoapp.database.Trip

class YourTripsAdapter : RecyclerView.Adapter<YourTripsAdapter.ViewHolder>() {

    var data =  listOf<Trip>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tripTitle: TextView = itemView.findViewById(R.id.trip_item_title)
        val tripDescription: TextView = itemView.findViewById(R.id.trip_item_description)

        fun bind(item: Trip) {
            //val res = holder.itemView.context.resources
            tripTitle.text = item.tripTitle
            tripDescription.text = item.tripDescription
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.card_view_your_trip_item, parent, false)

                return ViewHolder(view)
            }
        }
    }

}