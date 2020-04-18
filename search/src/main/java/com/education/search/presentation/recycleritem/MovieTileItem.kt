package com.education.search.presentation.recycleritem

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.education.search.R
import com.education.search.domain.entity.DomainMovie
import com.education.search.isShouldBeGreen
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.movie_item_tile.view.*


data class MovieTileItem(
    private val domainMovie: DomainMovie,
    private val onClickCallBack: (DomainMovie) -> Unit
): Item() {

    override fun bind(
        viewHolder: ViewHolder,
        position: Int
    ) {
        with(viewHolder.itemView) {
                Glide.with(posterImageView)
                    .load(domainMovie.posterPath)
                    .placeholder(resources.getDrawable(R.drawable.image_placeholder))
                    .transform(CenterCrop(), RoundedCorners(resources.getInteger(R.integer.round_corner_8)))
                    .into(posterImageView)

            titleTextView.text = domainMovie.title
            originalTitleTextView.text = domainMovie.originalTitle
            genreTextView.text = domainMovie.genre
            averageTextView.text = domainMovie.voteAverage.toString()
            if (isShouldBeGreen(domainMovie.voteAverage))
                averageTextView.setTextColor(resources.getColor(R.color.green_color))
            votesCountTextView.text = domainMovie.voteCount.toString()
            movieItemContainer.setOnClickListener {
                onClickCallBack.invoke(domainMovie)
            }

            val minWord = R.string.ru_locale_min
            runTimeTextView.text = "${domainMovie.runTime} ${resources.getString(minWord)}"
        }
    }

    override fun getLayout(): Int = R.layout.movie_item_tile

    override fun getId(): Long = domainMovie.hashCode().toLong()
}

