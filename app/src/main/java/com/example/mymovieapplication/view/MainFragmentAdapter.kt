package com.example.mymovieapplication.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapplication.R
import com.example.mymovieapplication.model.Movie


class MainFragmentAdapter(private var onItemViewClickListener: MainFragment.OnItemViewClickListener?) :
    RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>() {

    private var movieData: List<Movie> = listOf()

    fun setMovie(data: List<Movie>) {
        movieData = data
    }

    fun getMovie(): List<Movie> = movieData

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainFragmentAdapter.MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.main_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainFragmentAdapter.MainViewHolder, position: Int) {
        holder.bind(movieData[position])
    }

    override fun getItemCount(): Int {
        return movieData.size
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(movie: Movie) {
            itemView.findViewById<TextView>(R.id.item_movie_name).text = movie.name
            itemView.findViewById<ImageView>(R.id.item_movie_img).setImageResource(R.drawable.movie)
            itemView.findViewById<LinearLayout>(R.id.item_container).setOnClickListener {
                onItemViewClickListener?.onItemViewClick(movie)
            }
        }
    }

    fun removeListener() {
        onItemViewClickListener = null
    }


}