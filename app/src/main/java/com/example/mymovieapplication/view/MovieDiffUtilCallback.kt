package com.example.mymovieapplication.view

import androidx.recyclerview.widget.DiffUtil
import com.example.mymovieapplication.model.Movie

class MovieDiffUtilCallback(
    private val oldList: List<Movie>,
    private val newList: List<Movie>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldMovie: Movie = oldList[oldItemPosition]
        val newMovie: Movie = newList[newItemPosition]
        return oldMovie.id == newMovie.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldMovie: Movie = oldList[oldItemPosition]
        val newMovie: Movie = newList[newItemPosition]
        return oldMovie.name == newMovie.name
                && oldMovie.year == newMovie.year
                && oldMovie.rating == newMovie.rating
                && oldMovie.description == newMovie.description
    }
}