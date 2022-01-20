package com.example.mymovieapplication.view

import android.graphics.Typeface
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapplication.R
import com.example.mymovieapplication.model.Category


class MainFragmentAdapter(private var onItemViewClickListener: MainFragment.OnItemViewClickListener?) :
    RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>() {

    private var categoryData: List<Category> = listOf()

    fun setCategory(data: List<Category>) {
        categoryData = data
        //notifyDataSetChanged()
    }

    fun getCategory(): List<Category> = categoryData

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
        holder.bind(categoryData[position])
    }

    override fun getItemCount(): Int {
        return categoryData.size
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(category: Category) {
            itemView.findViewById<TextView>(R.id.category_name).text = category.name
            val categoryLinearLayout: LinearLayout =
                itemView.findViewById<LinearLayout>(R.id.list_movies)

            category.listOfMovie.forEach { movie ->
                val movieLinearLayout = LinearLayout(itemView.context)
                movieLinearLayout.orientation = LinearLayout.VERTICAL
                val params1 = LinearLayout.LayoutParams(
                    500,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                movieLinearLayout.layoutParams = params1
                movieLinearLayout.setPadding(20, 20, 20, 20)

                movieLinearLayout.setOnClickListener {
                    onItemViewClickListener?.onItemViewClick(movie)
                }

                val movieName = TextView(itemView.context)
                movieName.text = movie.name
                movieName.textSize = 20F
                movieName.typeface = Typeface.DEFAULT_BOLD
                movieName.setPadding(10)
                val paramsName = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    150
                )
                movieName.layoutParams = paramsName
                movieName.maxLines = 2
                movieName.ellipsize = TextUtils.TruncateAt.END


                val movieImg = ImageView(itemView.context)
                val paramsImg = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    250
                )
                movieImg.layoutParams = paramsImg
                movieImg.setImageResource(R.drawable.movie)
                movieImg.scaleType = ImageView.ScaleType.CENTER_CROP

                val movieYear = TextView(itemView.context)
                movieYear.text = movie.year
                movieYear.setPadding(10)

                val movieDescription = TextView(itemView.context)
                movieDescription.text = movie.description
                movieDescription.maxLines = 3
                movieDescription.ellipsize = TextUtils.TruncateAt.END
                movieDescription.setPadding(10)
                movieLinearLayout.addView(movieName)
                movieLinearLayout.addView(movieImg)
                movieLinearLayout.addView(movieYear)
                movieLinearLayout.addView(movieDescription)
                categoryLinearLayout.addView(movieLinearLayout)
            }

            /*itemView.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    category.name,
                    Toast.LENGTH_LONG
                ).show()
            }*/
        }
    }

    fun removeListener() {
        onItemViewClickListener = null
    }


}