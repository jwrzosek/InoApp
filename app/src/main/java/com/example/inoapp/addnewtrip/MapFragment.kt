package com.example.inoapp.addnewtrip


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels

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



    private val viewModel: AddNewTripViewModel by navGraphViewModels(R.id.addNewTripNavigation) {
        defaultViewModelProviderFactory
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        map_view.onCreate(savedInstanceState)
        map_view.onResume()

        map_view.getMapAsync(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentMapBinding>(inflater,
            R.layout.fragment_map, container,false)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.addNewTripViewModel = viewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = this

        //The complete onClickListener with Navigation
        binding.mapAddLocationButton.setOnClickListener { view : View ->
            view.findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.let{
            googleMap = it
        }

        // todo: add logic for picking location that open map on user location
        // delete hardcoded values
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
            viewModel.onCoordinatesChanged(latLng.latitude, latLng.longitude)
        }
    }



}
