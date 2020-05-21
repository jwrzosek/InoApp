package com.example.inoapp.addnewtrip


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.inoapp.R
import com.example.inoapp.database.InoDatabase
import com.example.inoapp.databinding.FragmentAddNewTripBinding

/**
 * A simple [Fragment] subclass.
 *
 */
class AddNewTripFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentAddNewTripBinding>(inflater,
            R.layout.fragment_add_new_trip, container,false)

        // Getting application context
        val application = requireNotNull(this.activity).application

        // Create an instance of the database
        val dataSource = InoDatabase.getInstance(application).tripDatabaseDao

        // Create viewModel
        val viewModel: AddNewTripViewModel by navGraphViewModels(R.id.addNewTripNavigation) {
            AddNewTripViewModelFactory(dataSource)
        }

        // Bind viewModel to layout data
        binding.addNewTripViewModel = viewModel

        binding.lifecycleOwner = this

        viewModel.navigateToHomeFragment.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigateUp()
                viewModel.doneNavigating()
            }
        })

        // if data is incomplete then show Toast to user
        viewModel.showIncompleteDataToast.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                Toast.makeText(application, getString(R.string.add_new_trip_complete_info_statement), Toast.LENGTH_SHORT).show()
                viewModel.doneShowingToast()
            }
        })

        // if user try to delete last point when there are no points on a list show Toast to user
        viewModel.showNoPointsStatement.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                Toast.makeText(application, getString(R.string.add_new_trip_no_points_statement), Toast.LENGTH_SHORT).show()
                viewModel.doneShowingToast()
            }
        })

        //The complete onClickListener with Navigation
        binding.addNewTripAddNewPoint.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_addNewTripFragment_to_addNewPointFragment)
        }

        return binding.root
    }


}
