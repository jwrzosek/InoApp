package com.example.inoapp.yourtrips

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.inoapp.database.Trip


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