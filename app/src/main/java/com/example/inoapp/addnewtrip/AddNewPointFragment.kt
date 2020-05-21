package com.example.inoapp.addnewtrip


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.inoapp.R
import com.example.inoapp.database.InoDatabase
import com.example.inoapp.databinding.FragmentAddNewPointBinding

/**
 * A simple [Fragment] subclass.
 *
 */
class AddNewPointFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentAddNewPointBinding>(inflater,
            R.layout.fragment_add_new_point, container,false)

        // Getting application context
        val application = requireNotNull(this.activity).application

        // Create an instance of the database
        val dataSource = InoDatabase.getInstance(application).tripDatabaseDao

        // Create viewModel
        val viewModel: AddNewTripViewModel by navGraphViewModels(R.id.addNewTripNavigation) {
            //defaultViewModelProviderFactory
            AddNewTripViewModelFactory(dataSource)
        }

        binding.addNewTripViewModel = viewModel

        binding.lifecycleOwner = this

        //The complete onClickListener with Navigation
        binding.addNewPointBackButton.setOnClickListener { view : View ->
            view.findNavController().navigateUp()
        }

        viewModel.navigateToAddNewTripFragment.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigateUp()
                viewModel.doneNavigating()
            }
        })

        // if data is incomplete then show Toast to user
        viewModel.showIncompleteDataToast.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                Toast.makeText(application, getString(R.string.add_new_point_complete_info_statement), Toast.LENGTH_SHORT).show()
                viewModel.doneShowingToast()
            }
        })

        binding.addNewPointAddLocationButton.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_addNewPointFragment_to_mapFragment)
        }

        return binding.root
    }

}
