package com.education.search.presentation.recycleritem

import com.bumptech.glide.Glide
import com.education.core_api.GREEN_MIN_VOTE_AVERAGE
import com.education.search.R
import com.education.search.domain.entity.Movie
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.movie_item.*

data class MovieListItem(
    private val greenTextColorId: Int,
    private val minWord: String,
    private val onClickCallBack: (Movie) -> Unit,
    private val movie: Movie
): Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        with(viewHolder) {
            if (!movie.posterPath.contains("null", true))
                Glide.with(posterImageView).load(movie.posterPath).into(posterImageView)
            titleTextView.text = movie.title
            originalTitleTextView.text = movie.originalTitle
            genreTextView.text = movie.genre
            averageTextView.text = movie.voteAverage.toString()
            if (isShouldBeGreen(movie.voteAverage))
                averageTextView.setTextColor(greenTextColorId)
            votesCountTextView.text = movie.voteCount.toString()
            movieItemContainer.setOnClickListener {
                onClickCallBack.invoke(movie)
            }

            if (movie.runTime.isNotBlank())
                runTimeTextView.text = "${movie.runTime} $minWord"
        }
    }

    private fun isShouldBeGreen(voteAverage: Double): Boolean = voteAverage >= GREEN_MIN_VOTE_AVERAGE

    override fun getLayout(): Int = R.layout.movie_item

    override fun getId(): Long = movie.hashCode().toLong()
}