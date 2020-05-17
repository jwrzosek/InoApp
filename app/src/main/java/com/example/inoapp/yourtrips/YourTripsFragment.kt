package com.example.inoapp.yourtrips


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.inoapp.R
import com.example.inoapp.database.InoDatabase
import com.example.inoapp.databinding.FragmentYourTripsBinding


/**
 * A simple [Fragment] subclass.
 *
 */
class YourTripsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentYourTripsBinding>(inflater,
            R.layout.fragment_your_trips, container,false)

        // view Model
        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = InoDatabase.getInstance(application).tripDatabaseDao
        val viewModelFactory = YourTripsViewModelFactory(dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        val yourTripsViewModel = ViewModelProviders.of(
                this, viewModelFactory).get(YourTripsViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.yourTripsViewModel = yourTripsViewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = this

        // RecyclerView Adapter
        val adapter = YourTripsAdapter(TripClickListener {tripId ->
            Toast.makeText(context, "Typed trip id: $tripId", Toast.LENGTH_LONG).show()
            yourTripsViewModel.onTripClicked(tripId)
        })
        binding.yourTripsList.adapter = adapter

        yourTripsViewModel.navigateToTripDetail.observe(viewLifecycleOwner, Observer { tripId ->
            tripId?.let {
               this.findNavController().navigate(
                   YourTripsFragmentDirections.actionYourTripsFragmentToTripDetailsFragment(tripId))
                yourTripsViewModel.onTripDetailsNavigated()
            }

        })

        // Observe if list displaying by RecyclerView has changed
        yourTripsViewModel.trips.observe(viewLifecycleOwner, Observer {
            it?.let {
                // submitList() is a ListAdapter method to tell that a new version of a list is available
                adapter.submitList(it)
            }
        })

        //The complete onClickListener with Navigation // todo: delete later if not needed
       /* binding.yourTripsButton.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_yourTripsFragment_to_homeFragment)
        }*/
        return binding.root
    }
}
