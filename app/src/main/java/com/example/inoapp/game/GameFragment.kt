package com.example.inoapp.game

import android.Manifest
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.inoapp.R
import com.example.inoapp.database.InoDatabase
import com.example.inoapp.databinding.FragmentGameBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_game.*

/**
 * Fragment responsible for provide a trip game.
 */
class GameFragment : Fragment(), OnMapReadyCallback  {

    private lateinit var googleMap: GoogleMap

    private lateinit var gameViewModel: GameViewModel

    private val REQUEST_LOCATION_PERMISSION = 1

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    // Location updates
    private lateinit var lastLocation: Location
    // 1
    private lateinit var locationCallback: LocationCallback
    // 2
    private lateinit var locationRequest: LocationRequest
    //private var locationUpdateState = false

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        // 3
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        point_localization_map_view.onCreate(savedInstanceState)
        point_localization_map_view.onResume()

        point_localization_map_view.getMapAsync(this)
    }

    /** stop location update request */
    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    /** restart the location update request */
    override fun onResume() {
        super.onResume()
        startLocationUpdates()
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val currentPointIndex = sharedPref.getInt(getString(R.string.saved_current_point_index), 0)
        gameViewModel.updateCurrentPointIndex(currentPointIndex)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentGameBinding>(inflater,
            R.layout.fragment_game,container,false)

        // Read tripId from SharedPreferences
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return view
        val tripId = sharedPref.getLong(getString(R.string.saved_trip_id), 0L)
        val currentPointIndex = sharedPref.getInt(getString(R.string.saved_current_point_index), 0)

        val application = requireNotNull(this.activity).application
        val dataSource = InoDatabase.getInstance(application).tripDatabaseDao

        // Create an instance of the ViewModel Factory.
        val viewModelFactory = GameViewModelFactory(tripId, currentPointIndex, dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        gameViewModel = ViewModelProviders.of(
            this, viewModelFactory).get(GameViewModel::class.java)

        binding.gameViewModel = gameViewModel

        binding.lifecycleOwner = this

        // add observer for endTrip flag
        gameViewModel.endTrip.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                clearSharedPreferences()
                Toast.makeText(context, getString(R.string.game_left_the_game_statement), Toast.LENGTH_SHORT).show()
                this.findNavController().navigateUp()
            }
        })

        // add observer for navigateToQuiz flag
        gameViewModel.navigateToQuiz.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                this.findNavController().navigate(R.id.action_gameFragment_to_quizFragment)
                gameViewModel.doneNavigating()
            }
        })

        // fusedLocationClient.getLastLocation() gives you the most recent location currently available
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

        // callback for updating last location variable with new location coordinates
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(location: LocationResult) {
                super.onLocationResult(location)

                lastLocation = location.lastLocation

                gameViewModel.updateDistanceToNextPoint(lastLocation)
            }
        }

        // create a location request
        createLocationRequest()
        return binding.root
    }

    private fun startLocationUpdates() {
        // 1
        if (ActivityCompat.checkSelfPermission(requireNotNull(this.activity).application,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireNotNull(this.activity),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        //2
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
    }

    private fun createLocationRequest() {
        // 1
        // You create an instance of LocationRequest, add it to an instance of LocationSettingsRequest.
        // Builder and retrieve and handle any changes to be made based on the current state of the user’s location settings
        locationRequest = LocationRequest()
        // 2
        // interval specifies the rate at which your app will like to receive updates.
        locationRequest.interval = 10000
        // 3
        // fastestInterval specifies the fastest rate at which the app can handle updates.
        // Setting the fastestInterval rate places a limit on how fast updates will be sent to your app.
        locationRequest.fastestInterval = 8000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        // 4
        // Create a settings client and a task to check location settings.
        val client = LocationServices.getSettingsClient(requireNotNull(this.activity))
        val task = client.checkLocationSettings(builder.build())

        // 5
        // A task success means all is well and you can go ahead and initiate a location request.
        task.addOnSuccessListener {
            //locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            // 6
            // A task failure means the location settings have some issues which can be fixed.
            // This could be as a result of the user’s location settings turned off.
            // You fix this by showing the user a dialog as shown below
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(requireNotNull(this.activity),
                        REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
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
        val zoomLevel = 14f
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))

        enableMyLocation()
    }

    private fun isPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
            requireNotNull(this.activity),
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            googleMap.isMyLocationEnabled = true
        }
        else {
            ActivityCompat.requestPermissions(
                requireNotNull(this.activity),
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }


    /**
     * Since, saving data to SharedReferences needs activity reference so
     * this action is provided in saveCurrentPointIndexInSharedPreferences() method
     * inside GameFragment.
     */
    private fun saveCurrentPointIndexInSharedPreferences(currentPointIndex: Int) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt(getString(R.string.saved_current_point_index), currentPointIndex)
            apply()
        }
        Log.d("TripDetailsFragment", "saveCurrentPointIndexInSharedPreferences with id=$currentPointIndex")
    }

    /**
     * Since, saving data to SharedReferences needs activity reference so
     * this action is provided in saveTripIdInSharedPreferences() method
     * inside TripDetailsFragment.
     */
    private fun clearSharedPreferences() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putLong(getString(R.string.saved_trip_id), 0L)
            putInt(getString(R.string.saved_current_point_index), 0)
            putInt(getString(R.string.saved_trip_number_of_points), 0)
            apply()
        }
        Log.d("GameFragment", "saveTripIdInSharedPreferences with id=0L")
    }

}