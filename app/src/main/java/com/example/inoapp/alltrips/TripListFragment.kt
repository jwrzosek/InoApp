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
        val adapter = TripListAdapter(TripClickListener { tripId ->
            Toast.makeText(context, "Typed trip id: $tripId", Toast.LENGTH_LONG).show()
            //Log.d("YourTripsFragment", "Typed trip id: $tripId") // todo: delete later
            tripListViewModel.onTripClicked(tripId)
        })

        binding.tripListList.adapter = adapter

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
















