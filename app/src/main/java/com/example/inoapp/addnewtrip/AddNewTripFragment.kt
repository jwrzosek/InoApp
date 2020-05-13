package com.example.inoapp.addnewtrip


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    /*private val viewModel: AddNewTripViewModel by navGraphViewModels(R.id.addNewTripNavigation) {
        defaultViewModelProviderFactory
    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentAddNewTripBinding>(inflater,
            R.layout.fragment_add_new_trip, container,false)

        // Getting application context
        val application = requireNotNull(this.activity).application

        // Create an instance of the database
        val dataSource = InoDatabase.getInstance(application).tripDatabaseDao

        // Create viewModel
        val viewModel: AddNewTripViewModel by navGraphViewModels(R.id.addNewTripNavigation) {
            //defaultViewModelProviderFactory
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

        //The complete onClickListener with Navigation
        binding.addNewTripAddNewPoint.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_addNewTripFragment_to_addNewPointFragment)
        }

        /*binding.addNewTripAddButton.setOnClickListener { view : View ->
            view.findNavController().navigateUp()
        }*/

        return binding.root
    }


}
