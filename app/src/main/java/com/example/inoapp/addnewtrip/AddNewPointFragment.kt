package com.example.inoapp.addnewtrip


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.inoapp.R
import com.example.inoapp.databinding.FragmentAddNewPointBinding
import com.example.inoapp.databinding.FragmentAddNewTripBinding

/**
 * A simple [Fragment] subclass.
 *
 */
class AddNewPointFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentAddNewPointBinding>(inflater,
            R.layout.fragment_add_new_point, container,false)

        //The complete onClickListener with Navigation
        binding.addNewPointBackButton.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_addNewPointFragment_to_addNewTripFragment)
         }

        return binding.root
    }

}
