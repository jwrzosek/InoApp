package com.example.inoapp


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.inoapp.databinding.FragmentAddNewTripBinding

/**
 * A simple [Fragment] subclass.
 *
 */
class AddNewTripFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentAddNewTripBinding>(inflater,
            R.layout.fragment_add_new_trip, container,false)

        //The complete onClickListener with Navigation
        binding.addButton.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_addNewTripFragment_to_homeFragment)
        }

        return binding.root
    }


}
