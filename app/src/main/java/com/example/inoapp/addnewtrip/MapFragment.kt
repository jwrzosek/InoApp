package com.example.inoapp.addnewtrip


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController

import com.example.inoapp.R
import com.example.inoapp.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 *
 */
class MapFragment : Fragment(), OnMapReadyCallback {

    private val REQUEST_LOCATION_PERMISSION = 1

    private lateinit var googleMap: GoogleMap

    private lateinit var viewModelFactory: AddNewTripViewModelFactory

    private lateinit var addNewTripViewModel: AddNewTripViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        map_view.onCreate(savedInstanceState)
        map_view.onResume()

        map_view.getMapAsync(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentMapBinding>(inflater,
            R.layout.fragment_map, container,false)

        viewModelFactory = AddNewTripViewModelFactory()
        addNewTripViewModel = ViewModelProviders.of(
            this, viewModelFactory).get(AddNewTripViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.addNewTripViewModel = addNewTripViewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = this

        //The complete onClickListener with Navigation
        binding.mapAddLocationButton.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_mapFragment_to_addNewPointFragment)
        }



        return binding.root
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.let{
            googleMap = it
        }

        val latitude = 52.181510
        val longitude = 21.054533

        val homeLatLng = LatLng(latitude, longitude)
        val zoomLevel = 15f
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
        googleMap.addMarker(MarkerOptions().position(homeLatLng))

        setMapLongClick(googleMap)
    }

    private fun setMapLongClick(map : GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            // A Snippet is Additional text that's displayed below the title.
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )
            map.clear()
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(getString(R.string.dropped_pin))
                    .snippet(snippet)
            )
            addNewTripViewModel.onCoordinatesChanged(latLng.latitude, latLng.longitude)
        }
    }



}
