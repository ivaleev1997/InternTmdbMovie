package com.education.search.presentation.recycleritem

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.education.core_api.isShouldBeGreen
import com.education.search.R
import com.education.search.domain.entity.Movie
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.movie_item.view.*

data class MovieListItem(
    private val movie: Movie,
    private val onClickCallBack: (Movie) -> Unit
): Item() {
    override fun bind(
        viewHolder: ViewHolder,
        position: Int
    ) {
        with(viewHolder.itemView) {
            Glide.with(posterImageView)
                .load(movie.posterPath)
                .placeholder(resources.getDrawable(R.drawable.image_placeholder))
                .transform(CenterCrop(), RoundedCorners(8))
                .into(posterImageView)

            titleTextView.text = movie.title
            originalTitleTextView.text = movie.originalTitle
            genreTextView.text = movie.genre
            averageTextView.text = movie.voteAverage.toString()
            if (isShouldBeGreen(movie.voteAverage))
                averageTextView.setTextColor(resources.getColor(R.color.green_color))
            votesCountTextView.text = movie.voteCount.toString()
            movieItemContainer.setOnClickListener {
                onClickCallBack.invoke(movie)
            }

            if (movie.runTime.isNotBlank()) {
                val minWord = R.string.ru_locale_min
                runTimeTextView.text = "${movie.runTime} ${resources.getString(minWord)}"
            }
        }
    }

    override fun getLayout(): Int = R.layout.movie_item

    override fun getId(): Long = movie.hashCode().toLong()
}