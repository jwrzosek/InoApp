package com.example.inoapp.yourtrips

import android.location.Location
import android.os.Bundle
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.inoapp.R
import com.example.inoapp.database.Point
import com.example.inoapp.database.Trip
import com.example.inoapp.database.TripWithPoints
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_game.view.*


/**
 * This file provides binding adapters which are responsible for adapting
 * data into something that data binding can use to bind a view(e.g. text or an image)
 */

@BindingAdapter("tripTitle")
fun TextView.setTripTitle(item: Trip?) {
    item?.let {
        text = item.tripTitle
    }
}

@BindingAdapter("tripDescription")
fun TextView.setTripDescription(item: Trip?) {
    // null check because LiveData starts as a null
    item?.let {
        text = item.tripDescription
    }
}

@BindingAdapter("tripLocalization")
fun TextView.setTripLocalization(item: Trip?) {
    // null check because LiveData starts as a null
    item?.let {
        text = item.tripLocalization
    }
}

@BindingAdapter("tripNumberOfPoints")
fun TextView.setTripNumberOfPoints(item: Trip?) {
    // null check because LiveData starts as a null
    item?.let {
        text = item.numberOfPoints.toString()
    }
}


// Binding Adapters for GameFragment
@BindingAdapter("tripTitleForGame")
fun TextView.setTripTitleForGame(item: Trip?) {
    // null check because LiveData starts as a null
    item?.let {
        val stringFormatted = "Trip title:\n${item.tripTitle}"
        text = stringFormatted
    }
}

@BindingAdapter("tripDescriptionForGame")
fun TextView.setTripDescriptionForGame(item: Trip?) {
    // null check because LiveData starts as a null
    item?.let {
        val stringFormatted = "Trip title:\n${item.tripDescription}"
        text = stringFormatted
    }
}

@BindingAdapter(value=["tripWhichPointForGame:item", "tripWhichPointForGame:currentIndex"], requireAll = true)
fun TextView.setTripWhichPointForGame(item: Trip?, currentIndex: Int) {
    // null check because LiveData starts as a null
    item?.let {
        val stringFormatted = if(item.numberOfPoints >= currentIndex+1) "Point ${currentIndex + 1} of ${item.numberOfPoints}"
                                else ""
        text = stringFormatted
    }
}


@BindingAdapter("pointDescriptionForGame")
fun TextView.setPointDescriptionForGame(item: Point?) {
    // null check because LiveData starts as a null
    item?.let {
        val stringFormatted = "Point title:\n${item.pointDescription}"
        text = stringFormatted
    }
}

@BindingAdapter("distanceLeftForGame")
fun TextView.setDistanceLeftForGame(distance: Float) {
    val distanceLeft = distance.toInt()
    val stringFormatted = if (distanceLeft > 1) "Distance to next point:\n${distanceLeft} m"
                            else "Distance to next point:\nCounting distance..."
    text = stringFormatted
}

@BindingAdapter("googleMapsMarkerCoordinatesForGame")
fun MapView.setGoogleMapsMarkerForGame(item: Point?) {
    item?.let {
        if(point_localization_map_view != null) {
            point_localization_map_view.getMapAsync(OnMapReadyCallback() {googleMap ->
                googleMap.clear()
                val homeLatLng = LatLng(item.pointLatitude, item.pointLongitude)
                val zoomLevel = 14f
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
                googleMap.addMarker(MarkerOptions().position(homeLatLng))
            })
        }
    }
}




