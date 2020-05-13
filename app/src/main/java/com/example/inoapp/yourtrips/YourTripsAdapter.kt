package com.example.inoapp.yourtrips

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.inoapp.database.Trip
import com.example.inoapp.databinding.CardViewYourTripItemBinding

/**
 * Adapter class for RecyclerView used in YourTripsFragment
 */
class YourTripsAdapter : ListAdapter<Trip, YourTripsAdapter.ViewHolder>(YourTripsDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: CardViewYourTripItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Trip) {
            //val res = holder.binding.context.resources
            binding.tripItemTitle.text = item.tripTitle
            binding.tripItemDescription.text = item.tripDescription
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardViewYourTripItemBinding.inflate(layoutInflater, parent, false)
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




























