package com.example.inoapp.yourtrips

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.inoapp.database.Trip


/**
 * This file provides binding adapters which are responsible for adapting
 * data into something that data binding can use to bind a view(e.g. text or an image)
 */

@BindingAdapter("tripTitle")
fun TextView.setTripTitle(item: Trip) {
    text = item.tripTitle
}

@BindingAdapter("tripDescription")
fun TextView.setTripDescription(item: Trip) {
    text = item.tripDescription
}