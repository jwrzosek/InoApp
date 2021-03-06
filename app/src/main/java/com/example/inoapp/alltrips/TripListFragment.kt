package com.example.inoapp.alltrips


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.inoapp.R
import com.example.inoapp.database.InoDatabase
import com.example.inoapp.database.Trip
import com.example.inoapp.databinding.FragmentTripListBinding
import com.google.firebase.firestore.FirebaseFirestore


/**
 * A simple [Fragment] subclass.
 *
 */
class TripListFragment : Fragment() {

    private var trips = mutableListOf<Trip>()

    private var tripsId = mutableListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentTripListBinding>(inflater,
            R.layout.fragment_trip_list, container,false)

        // view Model
        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = InoDatabase.getInstance(application).tripDatabaseDao
        val viewModelFactory = TripListViewModelFactory(dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        val tripListViewModel = ViewModelProviders.of(
            this, viewModelFactory).get(TripListViewModel::class.java)

        binding.tripListViewModel = tripListViewModel

        binding.lifecycleOwner = this

        // RecyclerView Adapter
        val adapter = TripListAdapter(TripClickListener { tripTitle ->
            Toast.makeText(context, "Trip added: $tripTitle", Toast.LENGTH_SHORT).show()
            //Log.d("YourTripsFragment", "Typed trip id: $tripId") // todo: delete later
            tripListViewModel.onTripClicked(tripTitle)
        })

        binding.tripListList.adapter = adapter

        tripListViewModel.navigateToHomeFragment.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigateUp()
                tripListViewModel.doneNavigating()
            }
        })

        // Observe if list displaying by RecyclerView has changed
        tripListViewModel.trips.observe(viewLifecycleOwner, Observer {
            it?.let {
                // submitList() is a ListAdapter method to tell that a new version of a list is available
                adapter.submitList(it)
            }
        })

        return binding.root
    }
}
















