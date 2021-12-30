package com.example.mymovieapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mymovieapplication.R
import com.example.mymovieapplication.databinding.MainFragmentBinding
import com.example.mymovieapplication.model.Movie
import com.example.mymovieapplication.viewmodel.AppState
import com.example.mymovieapplication.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private val adapter = MainFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(movie: Movie) {
            val manager = activity?.supportFragmentManager
            if (manager != null) {
                val bundle = Bundle()
                bundle.putParcelable(DetailsFragment.BUNDLE_EXTRA, movie)
                manager.beginTransaction()
                    .add(R.id.container, DetailsFragment.newInstance(bundle))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        }

    })
    private var isDataSetRus: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainFragmentRecyclerView.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener { changeMovieDataSet() }
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            renderData(it)
        })
        viewModel.getMovieFromLocalSourceRus()
    }

    private fun changeMovieDataSet() {
        if (isDataSetRus) {
            viewModel.getMovieFromRemoteSourceEng()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_baseline_flag_24)
        } else {
            viewModel.getMovieFromLocalSourceRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_baseline_outlined_flag_24)
        }
        isDataSetRus = !isDataSetRus
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                adapter.setCategory(appState.movieData)
            }
            is AppState.Error -> {
                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
                Snackbar.make(
                    binding.root,
                    appState.error.message.toString(),
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction("Try again") { viewModel.getMovieFromLocalSourceRus() }
                    .show()
            }
            is AppState.Loading -> {
                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
        }
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(movie: Movie)
    }

    companion object {
        fun newInstance() = MainFragment()
    }


    override fun onDestroy() {
        adapter.removeListener()
        super.onDestroy()
        _binding = null
    }


}