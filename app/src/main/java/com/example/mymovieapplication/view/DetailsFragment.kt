package com.example.mymovieapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mymovieapplication.databinding.DetailsFragmentBinding
import com.example.mymovieapplication.databinding.MainFragmentBinding
import com.example.mymovieapplication.model.Movie
import com.example.mymovieapplication.viewmodel.AppState
import com.example.mymovieapplication.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class DetailsFragment : Fragment() {

    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Movie>(BUNDLE_EXTRA)?.let { movie ->
            with(binding) {
                detailsFragmentMovieName.text = movie.name
                detailsFragmentMovieYear.text = movie.year
                detailsFragmentMovieRating.text = movie.rating.toString()
                detailsFragmentMovieDescription.text = movie.description
            }
        }
    }

    companion object {
        const val BUNDLE_EXTRA = "MOVIE"

        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}