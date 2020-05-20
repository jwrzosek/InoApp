package com.example.inoapp.alltrips


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.inoapp.database.Trip
import com.example.inoapp.databinding.CardViewTripListItemBinding

/**
 * Adapter class for RecyclerView used in TripListFragment
 */
class TripListAdapter(val clickListener: TripClickListener) : ListAdapter<Trip, TripListAdapter.ViewHolder>(YourTripsDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: CardViewTripListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Trip, clickListener: TripClickListener) {
            binding.trip = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardViewTripListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
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

class TripClickListener(val clickListener: (tripTitle: String) -> Unit) {
    fun onClick(trip: Trip) = clickListener(trip.tripTitle)
}