package com.example.inoapp.quiz

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.inoapp.R
import com.example.inoapp.database.InoDatabase
import com.example.inoapp.databinding.FragmentQuizBinding

class QuizFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentQuizBinding>(
            inflater, R.layout.fragment_quiz, container, false)

        // Read tripId from SharedPreferences
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return view
        val tripId = sharedPref.getLong(getString(R.string.saved_trip_id), 0L)
        val currentPointIndex = sharedPref.getInt(getString(R.string.saved_current_point_index), 0)
        val tripNumberOfPoints = sharedPref.getInt(getString(R.string.saved_trip_number_of_points), 0)

        // view Model
        val application = requireNotNull(this.activity).application
        val dataSource = InoDatabase.getInstance(application).tripDatabaseDao

        // Create an instance of the ViewModel Factory.
        val viewModelFactory = QuizViewModelFactory(tripId, currentPointIndex, dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        val quizViewModel = ViewModelProviders.of(
            this, viewModelFactory).get(QuizViewModel::class.java)

        binding.quizViewModel = quizViewModel

        binding.lifecycleOwner = this

        // add observer for navigateBackToGame flag
        quizViewModel.navigateBackToGame.observe(viewLifecycleOwner, Observer {
            if(it == true) {

                // todo: update point current index with right condition
                if (currentPointIndex < (tripNumberOfPoints - 1)){
                    updateCurrentPointIndexInSharedPreferences(currentPointIndex)
                    this.findNavController().navigateUp()
                    quizViewModel.doneNavigating()
                } else {
                    Toast.makeText(context, "You end this trip!\nCongratulations!", Toast.LENGTH_LONG).show()
                    //todo: end of trip, clear sheardprefs and navigate to home fragment
                }

            }
        })

        // add observer for showRightAnswerToast flag
        quizViewModel.showRightAnswerToast.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                Toast.makeText(context, "Right answer!\nClick back button and keep exploring!", Toast.LENGTH_LONG).show()
                quizViewModel.doneShowingToast()
            }
        })

        // add observer for showWrongAnswerToast flag
        quizViewModel.showWrongAnswerToast.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                Toast.makeText(context, "Wrong answer :( Try again!", Toast.LENGTH_SHORT).show()
                quizViewModel.doneShowingToast()
            }
        })

        return binding.root
    }

    private fun updateCurrentPointIndexInSharedPreferences(nextIndex: Int) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt(getString(R.string.saved_current_point_index), nextIndex+1)
            apply()
        }
        Log.d("QuizFragment", "saveTripIdInSharedPreferences with id=${nextIndex+1}")
    }
}