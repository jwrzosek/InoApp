package com.example.inoapp.home


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.inoapp.R
import com.example.inoapp.databinding.FragmentHomeBinding

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater,
            R.layout.fragment_home,container,false)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return view
        val tripId = sharedPref.getLong(getString(R.string.saved_trip_id), 0L)

        val viewModelFactory = HomeViewModelFactory(tripId)

        // Get a reference to the ViewModel associated with this fragment.
        val homeViewModel = ViewModelProviders.of(
            this, viewModelFactory).get(HomeViewModel::class.java)

        binding.homeViewModel = homeViewModel

        binding.lifecycleOwner = this

        // observer for navigateToHome flag which mean user click start trip button
        homeViewModel.navigateToYourTrips.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigate(R.id.action_homeFragment_to_yourTripsFragment)
                homeViewModel.doneNavigating()
            }
        })

        // observer for navigateToHome flag which mean user click start trip button
        homeViewModel.navigateToTripList.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigate(R.id.action_homeFragment_to_tripListFragment)
                homeViewModel.doneNavigating()
            }
        })

        // observer for navigateToHome flag which mean user click start trip button
        homeViewModel.navigateToAddNewTrip.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigate(R.id.addNewTripNavigation)
                homeViewModel.doneNavigating()
            }
        })

        // observer for navigateToHome flag which mean user click start trip button
        homeViewModel.navigateToAbout.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigate(R.id.action_homeFragment_to_aboutFragment)
                homeViewModel.doneNavigating()
            }
        })

        Toast.makeText(context, "Shared preferences id: $tripId", Toast.LENGTH_LONG).show()

        return binding.root
    }
}