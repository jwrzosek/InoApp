package com.example.inoapp.yourtrips

import android.location.Location
import android.os.Bundle
import android.widget.Button
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

// BASIC ADAPTERS

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

// BINDING ADAPTERS FOR TripListFragment
@BindingAdapter("tripLocalizationForTripList")
fun TextView.setTripLocalizationForTripList(item: Trip?) {
    // null check because LiveData starts as a null
    item?.let {
        val stringFormatted = "Localization: ${item.tripLocalization}"
        text = stringFormatted
    }
}

@BindingAdapter("tripNumberOfPointsForTripList")
fun TextView.setTripNumberOfPointsForTripList(item: Trip?) {
    // null check because LiveData starts as a null
    item?.let {
        val stringFormatted = "Number of points: ${item.numberOfPoints}"
        text = stringFormatted
    }
}

// BINDING ADAPTERS FOR GameFragment
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

@BindingAdapter(value=["item", "currentIndex"], requireAll = true)
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

// BINDING ADAPTERS FOR TripDetailsFragment
@BindingAdapter("tripLocalizationForDetails")
fun TextView.setTripLocalizationForDetails(item: Trip?) {
    // null check because LiveData starts as a null
    item?.let {
        val stringFormatted = "Trip localization:\n${item.tripLocalization}"
        text = stringFormatted
    }
}

@BindingAdapter("tripNumberOfPointsForDetails")
fun TextView.setTripNumberOfPointsForDetails(item: Trip?) {
    // null check because LiveData starts as a null
    item?.let {
        val stringFormatted = "Trip localization:\n${item.numberOfPoints}"
        text = stringFormatted
    }
}

// BINDING ADAPTERS FOR GameFragment
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
            point_localization_map_view.getMapAsync { googleMap ->
                googleMap.clear()
                val homeLatLng = LatLng(item.pointLatitude, item.pointLongitude)
                val zoomLevel = 14f
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
                googleMap.addMarker(MarkerOptions().position(homeLatLng))
            }
        }
    }
}


// BINDING ADAPTERS FOR QuizFragment
@BindingAdapter("pointDescriptionForQuiz")
fun TextView.setPointDescriptionForQuiz(item: Point?) {
    // null check because LiveData starts as a null
    item?.let {
        val stringFormatted = "Point description:\n${item.pointDescription}"
        text = stringFormatted
    }
}
@BindingAdapter("questionForQuiz")
fun TextView.setQuestionForQuiz(item: Point?) {
    // null check because LiveData starts as a null
    item?.let {
        val stringFormatted = "Question:\n${item.pointQuestion}"
        text = stringFormatted
    }
}

@BindingAdapter(value=["item", "whichButton"], requireAll = true)
fun Button.setAnswerForQuiz(item: Point?, whichButton: Int) {
    // null check because LiveData starts as a null
    item?.let {
        text = when (whichButton) {
            0 -> {
                item.rightAnswer
            }
            1 -> {
                item.wrongAnswer1
            }
            2 -> {
                item.wrongAnswer2
            }
            else -> {
                "blad"
            }
        }
    }
}








