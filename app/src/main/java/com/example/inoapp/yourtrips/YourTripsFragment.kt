package com.example.inoapp.yourtrips


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
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

        // Setting the support action bar title
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_your_trips_fragment)

        // view Model
        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = InoDatabase.getInstance(application).tripDatabaseDao
        val viewModelFactory = YourTripsViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
        val yourTripsViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(YourTripsViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.yourTripsViewModel = yourTripsViewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.setLifecycleOwner(this)

        val adapter = YourTripsAdapter()
        binding.yourTripsList.adapter = adapter

        yourTripsViewModel.trips.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })

        //The complete onClickListener with Navigation
       /* binding.yourTripsButton.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_yourTripsFragment_to_homeFragment)
        }*/
        return binding.root
    }
}
