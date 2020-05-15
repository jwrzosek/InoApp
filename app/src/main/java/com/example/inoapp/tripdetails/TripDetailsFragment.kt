package com.example.inoapp.tripdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.inoapp.R
import com.example.inoapp.database.InoDatabase
import com.example.inoapp.databinding.FragmentTripDetailsBinding
import com.example.inoapp.yourtrips.YourTripsViewModelFactory

/**
 * Fragment for showing trip details.
 */
class TripDetailsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentTripDetailsBinding>(inflater,
            R.layout.fragment_trip_details, container,false)

        // view Model
        val application = requireNotNull(this.activity).application
        val arguments = TripDetailsFragmentArgs.fromBundle(arguments)

        // Create an instance of the ViewModel Factory.
        val dataSource = InoDatabase.getInstance(application).tripDatabaseDao
        val viewModelFactory = TripDetailsViewModelFactory(arguments.tripIdKey ,dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        val tripDetailsViewModel = ViewModelProviders.of(
                this, viewModelFactory).get(TripDetailsViewModel::class.java)

        binding.tripDetailsViewModel = tripDetailsViewModel

        binding.lifecycleOwner = this

        return binding.root
    }
}