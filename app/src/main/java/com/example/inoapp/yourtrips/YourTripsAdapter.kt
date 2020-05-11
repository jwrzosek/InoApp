package com.example.inoapp.yourtrips

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.inoapp.R
import com.example.inoapp.database.Trip

class YourTripsAdapter : ListAdapter<Trip, YourTripsAdapter.ViewHolder>(YourTripsDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
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


/**
 * This class is used by ListAdapter to figure out what changed in the list
 */
class YourTripsDiffCallback : DiffUtil.ItemCallback<Trip>() {
    override fun areItemsTheSame(oldItem: Trip, newItem: Trip): Boolean {
        return oldItem.tripId == newItem.tripId
    }

    override fun areContentsTheSame(oldItem: Trip, newItem: Trip): Boolean {
        return oldItem == newItem // data classes automatically define equals so we can do this kind of check
    }

}




























