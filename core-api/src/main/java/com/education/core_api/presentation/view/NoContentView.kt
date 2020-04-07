package com.education.core_api.presentation.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.RawRes
import com.education.core_api.R
import kotlinx.android.synthetic.main.no_content_overview.view.*

class NoContentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.no_content_overview, this, true)

        lateinit var attrsArray: TypedArray

        try {
            attrsArray = context.obtainStyledAttributes(attrs, R.styleable.NoContentView)
            with (attrsArray) {
                stubAnimation = getResourceId(R.styleable.NoContentView_animationView, R.raw.spider_animation)
                stubDescription = getString(R.styleable.NoContentView_descriptionText) ?: ""
            }
        } finally {
            attrsArray.recycle()
        }
    }

    var stubDescription: String = ""
        set(value) {
            field = value
            emptyMessage.text = field
        }

    @RawRes
    var stubAnimation: Int = R.raw.spider_animation
        set(value) {
            field = value
            animationView.setAnimation(field)
        }
}