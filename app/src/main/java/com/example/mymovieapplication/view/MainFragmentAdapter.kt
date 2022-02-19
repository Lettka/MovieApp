package com.example.mymovieapplication.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.mymovieapplication.R
import com.example.mymovieapplication.model.Movie


class MainFragmentAdapter(
    var context: Context,
    private var onItemViewClickListener: MainFragment.OnItemViewClickListener?
) :
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
            /*itemView.findViewById<ImageView>(R.id.item_movie_img).load("https://picsum.photos/300/300"){
                crossfade(true)
                placeholder(R.drawable.movie)
            }*/

            Log.d(
                "DEBUGLOG",
                "https://www.themoviedb.org/t/p/w300_and_h450_bestv2${movie.imagePath}"
            )

            val request = ImageRequest.Builder(context)
                //.data("https://www.svgrepo.com/show/45328/attachment-diagonal-interface-symbol-of-paperclip.svg")
                .data("https://www.themoviedb.org/t/p/w300_and_h450_bestv2${movie.imagePath}")
                .target(itemView.findViewById<ImageView>(R.id.item_movie_img))
                .placeholder(R.drawable.movie)
                .build()

            ImageLoader.Builder(context)
                /*.componentRegistry {
                    add(SvgDecoder(context))
                }*/
                .build()
                .enqueue(request)

            itemView.findViewById<LinearLayout>(R.id.item_container).setOnClickListener {
                onItemViewClickListener?.onItemViewClick(movie)
            }
        }
    }

    fun removeListener() {
        onItemViewClickListener = null
    }


}