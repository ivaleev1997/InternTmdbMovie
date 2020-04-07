package com.education.search.presentation.recycleritem

import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.education.core_api.isShouldBeGreen
import com.education.search.R
import com.education.search.domain.entity.Movie
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.movie_item_tile.*


data class MovieTileItem(
    private val greenTextColorId: Int,
    private val minWord: String,
    private val onClickCallBack: (Movie) -> Unit,
    private val movie: Movie,
    private val placeHolder: Drawable?
): Item() {

    override fun bind(
        viewHolder: ViewHolder,
        position: Int
    ) {
        with(viewHolder) {
                Glide.with(posterImageView)
                    .load(movie.posterPath)
                    .placeholder(placeHolder)
                    .transform(CenterCrop(), RoundedCorners(8))
                    .into(posterImageView)

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

    override fun getLayout(): Int = R.layout.movie_item_tile

    override fun getId(): Long = movie.hashCode().toLong()
}

