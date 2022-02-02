package com.example.mymovieapplication.view

import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapplication.R
import com.example.mymovieapplication.databinding.MainFragmentBinding
import com.example.mymovieapplication.model.Category
import com.example.mymovieapplication.model.CategoryDTO
import com.example.mymovieapplication.model.CategoryLoader
import com.example.mymovieapplication.model.Movie
import com.example.mymovieapplication.viewmodel.AppState
import com.example.mymovieapplication.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private var isDataSetRus: Boolean = true

    private var adapterList: MutableMap<String, MainFragmentAdapter> = mutableMapOf()

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
                    displayCategory(appState.categoryData)
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

    private fun displayCategory(lisOfCategory: List<Category>) {
        binding.linearLayoutForRecycle.removeAllViews()

        for (category in lisOfCategory) {

            when (adapterList[category.name]) {
                null -> adapterList[category.name] =
                    MainFragmentAdapter(object : OnItemViewClickListener {
                        override fun onItemViewClick(movie: Movie) {
                            activity?.supportFragmentManager?.apply {
                                beginTransaction()
                                    .add(
                                        R.id.container,
                                        DetailsFragment.newInstance(Bundle().apply {
                                            putParcelable(
                                                DetailsFragment.BUNDLE_EXTRA,
                                                movie
                                            )
                                        })
                                    )
                                    .addToBackStack("")
                                    .commitAllowingStateLoss()
                            }
                        }
                    })
            }

            val adapter = adapterList[category.name]

            val recyclerView = RecyclerView(requireActivity()).apply {
                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                this.adapter = adapter
                setPadding(0, 0, 0, 50)
            }

            val categoryName = TextView(requireContext()).apply {
                text = category.name
                textSize = 30F
                typeface = Typeface.DEFAULT_BOLD
                setPadding(40, 0, 0, 0)
                maxLines = 1
                ellipsize = TextUtils.TruncateAt.END
            }

            CategoryLoader.load(
                category,
                object : CategoryLoader.OnCategoryLoadListener {
                    override fun onLoaded(categoryDTO: CategoryDTO) {
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
                        val movieDiffUtilCallback: MovieDiffUtilCallback =
                            MovieDiffUtilCallback(
                                adapter?.let { it.getMovie() } ?: emptyList(),
                                category.listOfMovie
                            )

                        adapter?.let {
                            val categoryDiffResult =
                                DiffUtil.calculateDiff(movieDiffUtilCallback)
                            adapter.setMovie(category.listOfMovie)
                            categoryDiffResult.dispatchUpdatesTo(adapter)
                        }

                        binding.linearLayoutForRecycle.addView(categoryName)
                        binding.linearLayoutForRecycle.addView(recyclerView)
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
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(movie: Movie)
    }

    companion object {
        fun newInstance() = MainFragment()
    }


    override fun onDestroy() {
        for (adapter in adapterList) {
            adapter.value.removeListener()
        }
        super.onDestroy()
        _binding = null
    }

}