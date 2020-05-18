package com.example.inoapp.tripdetails

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
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
        val viewModelFactory = TripDetailsViewModelFactory(arguments.tripIdKey, dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        val tripDetailsViewModel = ViewModelProviders.of(
                this, viewModelFactory).get(TripDetailsViewModel::class.java)

        binding.tripDetailsViewModel = tripDetailsViewModel

        binding.lifecycleOwner = this

        // observer for navigateToHome flag which mean user click start trip button
        tripDetailsViewModel.navigateToHomeScreen.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigate(R.id.action_tripDetailsFragment_to_homeFragment)
                saveTripIdInSharedPreferences(arguments.tripIdKey)
                tripDetailsViewModel.doneNavigating()
            }
        })

        // observer for navigateToYourTrips flag which mean user click back button
        tripDetailsViewModel.navigateToYourTrips.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigateUp()
                tripDetailsViewModel.doneNavigating()
            }
        })

        return binding.root
    }

    /**
     * Since, saving data to SharedReferences needs activity reference so
     * this action is provided in saveTripIdInSharedPreferences() method
     * inside TripDetailsFragment.
     */
    private fun saveTripIdInSharedPreferences(tripId: Long) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putLong(getString(R.string.saved_trip_id), tripId)
            apply()
        }
        Log.d("TripDetailsFragment", "saveTripIdInSharedPreferences with id=$tripId")
    }
}