package com.example.inoapp.yourtrips


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.inoapp.R
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

        //The complete onClickListener with Navigation
        binding.yourTripsButton.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_yourTripsFragment_to_homeFragment)
        }
        return binding.root
    }
}
