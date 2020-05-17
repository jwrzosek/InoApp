package com.example.inoapp.game

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.inoapp.R
import com.example.inoapp.databinding.FragmentGameBinding

/**
 * Fragment responsible for provide a trip game.
 */
class GameFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentGameBinding>(inflater,
            R.layout.fragment_game,container,false)

        Toast.makeText(context, "Shared preferences id: ", Toast.LENGTH_LONG).show()

        binding.gameClearSharedPreferences.setOnClickListener {view : View ->
            clearSharedPreferences()
            view.findNavController().navigateUp()
        }

        return binding.root
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
            apply()
        }
        Log.d("GameFragment", "saveTripIdInSharedPreferences with id=0L")
    }
}