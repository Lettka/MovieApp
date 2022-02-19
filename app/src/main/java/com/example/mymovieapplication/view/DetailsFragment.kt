package com.example.mymovieapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.mymovieapplication.R
import com.example.mymovieapplication.databinding.DetailsFragmentBinding
import com.example.mymovieapplication.model.Movie

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

                val request = ImageRequest.Builder(requireActivity())
                    .data("https://www.themoviedb.org/t/p/w600_and_h900_bestv2${movie.imagePath}")
                    .target(detailsFragmentMovieImg)
                    .placeholder(R.drawable.movie)
                    .build()

                ImageLoader.Builder(requireActivity())
                    .build()
                    .enqueue(request)
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