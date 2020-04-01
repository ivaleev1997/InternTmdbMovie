package com.education.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.education.core_api.GREEN_MIN_VOTE_AVERAGE
import com.education.core_api.autoNotify
import com.education.search.entity.Movie
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.movie_item.*
import timber.log.Timber
import kotlin.properties.Delegates

class MovieAdapter(
    private val greenTextColorId: Int,
    private val onClickCallBack: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    var listMovies: List<Movie> by Delegates.observable(initialValue = listOf()) { prop, oldList, newList ->
        autoNotify(oldList, newList) { old, new ->
            old.id == new.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)

        return ViewHolder(onClickCallBack, view, greenTextColorId)
    }

    override fun getItemCount(): Int {
        return listMovies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Timber.d(listMovies[position].toString())
        holder.bindMovie(listMovies[position])
    }

    class ViewHolder(
        private val clickCallback: (Movie) -> Unit,
        override val containerView: View,
        private val greenId: Int
    ) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bindMovie(movie: Movie) {
            with(movie) {
                if (!posterPath.contains("null", true))
                    Glide.with(posterImageView).load(posterPath).into(posterImageView)
                titleTextView.text = title
                originalTitleTextView.text = originalTitle
                genreTextView.text = genre
                averageTextView.text = voteAverage.toString()
                if (isShouldBeGreen(voteAverage))
                    averageTextView.setTextColor(greenId)
                votesCountTextView.text = voteCount.toString()
                movieItemContainer.setOnClickListener {
                    clickCallback.invoke(this)
                }
            }
        }

        private fun isShouldBeGreen(voteAverageString: Double): Boolean {
            return voteAverageString >= GREEN_MIN_VOTE_AVERAGE
        }
    }
}