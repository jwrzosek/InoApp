package com.example.inoapp.addnewtrip


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.example.inoapp.R
import com.example.inoapp.databinding.FragmentAddNewPointBinding
import com.example.inoapp.databinding.FragmentAddNewTripBinding

/**
 * A simple [Fragment] subclass.
 *
 */
class AddNewPointFragment : Fragment() {

    private val viewModel: AddNewTripViewModel by navGraphViewModels(R.id.addNewTripNavigation) {
        defaultViewModelProviderFactory
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentAddNewPointBinding>(inflater,
            R.layout.fragment_add_new_point, container,false)

        binding.addNewTripViewModel = viewModel

        binding.lifecycleOwner = this

        //The complete onClickListener with Navigation
        binding.addNewPointBackButton.setOnClickListener { view : View ->
            view.findNavController().navigateUp()
        }

        binding.addNewPointAddButton.setOnClickListener { view : View ->
            view.findNavController().navigateUp()
            // todo: add point action
        }

        binding.addNewPointAddLocationButton.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_addNewPointFragment_to_mapFragment)
        }

        return binding.root
    }

}
