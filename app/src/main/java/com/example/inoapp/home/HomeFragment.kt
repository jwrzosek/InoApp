package com.example.inoapp.home


import android.content.Context
import android.os.Bundle
import android.util.Log
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

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater,
            R.layout.fragment_home,container,false)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return view
        val tripId = sharedPref.getLong(getString(R.string.saved_trip_id), 0L)

        val viewModelFactory = HomeViewModelFactory(tripId)

        // Get a reference to the ViewModel associated with this fragment.
        homeViewModel = ViewModelProviders.of(
            this, viewModelFactory).get(HomeViewModel::class.java)

        binding.homeViewModel = homeViewModel

        binding.lifecycleOwner = this

        // observer for selectedTripId; if != 0L it means that user has already selected trip
        // and there is a need to navigate to GameFragment with this ID
        /*homeViewModel.selectedTripId.observe(viewLifecycleOwner, Observer {
            if (it != 0L) { // Observed state is != 0L which means that user has selected trip
                this.findNavController().navigate(R.id.action_homeFragment_to_gameFragment)
            }
        })*/

        // observer for navigateToGame flag
        homeViewModel.navigateToGame.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                // todo: pass the trip id
                this.findNavController().navigate(R.id.action_homeFragment_to_gameFragment)
                homeViewModel.doneNavigating()
            }
        })

        // observer for navigateToYourTrips flag
        homeViewModel.navigateToYourTrips.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigate(R.id.action_homeFragment_to_yourTripsFragment)
                homeViewModel.doneNavigating()
            }
        })

        // observer for navigateToTripList flag
        homeViewModel.navigateToTripList.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigate(R.id.action_homeFragment_to_tripListFragment)
                homeViewModel.doneNavigating()
            }
        })

        // observer for navigateToAddNewTrip flag
        homeViewModel.navigateToAddNewTrip.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigate(R.id.addNewTripNavigation)
                homeViewModel.doneNavigating()
            }
        })

        // observer for navigateToAbout
        homeViewModel.navigateToAbout.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigate(R.id.action_homeFragment_to_aboutFragment)
                homeViewModel.doneNavigating()
            }
        })

        //Toast.makeText(context, "Shared preferences id: $tripId", Toast.LENGTH_LONG).show()
        Log.d("HomeFragment", "Shared preferences trip id: $tripId") // todo: delete later

        return binding.root
    }

    /**
     * When using backStack to navigate back from another fragment we need to check
     * if there wasn't any changes in current selected trip id.
     * if was we need to update viewModel which stores our LiveData to keep UI up to date
     */
    override fun onResume() {
        super.onResume()
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val tripId = sharedPref.getLong(getString(R.string.saved_trip_id), 0L)
        homeViewModel.updateTripId(tripId)
    }
}