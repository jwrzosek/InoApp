package com.example.inoapp


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.inoapp.databinding.FragmentTripListBinding


/**
 * A simple [Fragment] subclass.
 *
 */
class TripListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentTripListBinding>(inflater,
            R.layout.fragment_trip_list,container,false)
        return binding.root
    }
}
