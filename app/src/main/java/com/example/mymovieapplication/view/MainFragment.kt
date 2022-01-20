package com.example.mymovieapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import com.example.mymovieapplication.R
import com.example.mymovieapplication.databinding.MainFragmentBinding
import com.example.mymovieapplication.model.CategoryDTO
import com.example.mymovieapplication.model.CategoryLoader
import com.example.mymovieapplication.model.Movie
import com.example.mymovieapplication.viewmodel.AppState
import com.example.mymovieapplication.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private var isDataSetRus: Boolean = true

    private val adapter = MainFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(movie: Movie) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .add(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(DetailsFragment.BUNDLE_EXTRA, movie)
                    }))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        }
    })

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
        }.also { isDataSetRus = !isDataSetRus }
    }

    private fun renderData(appState: AppState) {
        with(binding) {
            when (appState) {
                is AppState.Success -> {
                    mainFragmentLoadingLayout.hide()

                    for (category in appState.movieData) {
                        CategoryLoader.load(
                            category,
                            object : CategoryLoader.OnCategoryLoadListener {
                                override fun onLoaded(categoryDTO: CategoryDTO) {
                                    //category.listOfMovie = emptyList()
                                    for (movie in categoryDTO.movies) {
                                        val itemMovie = Movie(
                                            movie.id.toString(),
                                            movie.title.toString(),
                                            movie.releaseDate.toString(),
                                            movie.rating,
                                            movie.overview.toString(),
                                            false
                                        )
                                        category.listOfMovie += itemMovie
                                    }
                                }

                                override fun onFailed(throwable: Throwable) {
                                    Toast.makeText(
                                        requireContext(),
                                        throwable.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                            }
                        )
                    }

                    val categoryDiffUtilCallback: CategoryDiffUtilCallback =
                        CategoryDiffUtilCallback(adapter.getCategory(), appState.movieData)
                    val categoryDiffResult = DiffUtil.calculateDiff(categoryDiffUtilCallback)
                    adapter.setCategory(appState.movieData)
                    categoryDiffResult.dispatchUpdatesTo(adapter)
                }
                is AppState.Error -> {
                    mainFragmentLoadingLayout.show()
                    root.showSnackBar(appState.error.message.toString(),
                        getString(R.string.reload),
                        {
                            viewModel.getMovieFromLocalSourceRus()
                        })
                }
                is AppState.Loading -> {
                    mainFragmentLoadingLayout.show()
                }
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