package com.example.inoapp.yourtrips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.inoapp.R
import com.example.inoapp.databinding.FragmentTripDetailsBinding

/**
 * Fragment for showing trip details.
 */
class TripDetailsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentTripDetailsBinding>(inflater,
            R.layout.fragment_trip_details, container,false)

        return binding.root
    }
}