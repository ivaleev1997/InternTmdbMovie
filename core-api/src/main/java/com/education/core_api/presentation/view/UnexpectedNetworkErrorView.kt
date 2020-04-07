package com.education.core_api.presentation.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.RawRes
import com.education.core_api.R
import com.education.core_api.extension.makeVisible
import kotlinx.android.synthetic.main.no_content_overview.view.animationView
import kotlinx.android.synthetic.main.no_internet_connection.view.*

class UnexpectedNetworkErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.no_internet_connection, this, true)

        lateinit var attrsArray: TypedArray

        try {
            attrsArray = context.obtainStyledAttributes(attrs, R.styleable.UnexpectedNetworkErrorView)
            with (attrsArray) {
                stubAnimation = getResourceId(R.styleable.UnexpectedNetworkErrorView_animationView, R.raw.no_internet_connection_animation)
                stubNoConnectionMessage = getString(R.styleable.UnexpectedNetworkErrorView_noConnectionMessage) ?: resources.getString(R.string.unexpected_network_error_string)
                stubRetryButton = getString(R.styleable.UnexpectedNetworkErrorView_retryButtonText) ?: ""
            }
        } finally {
            attrsArray.recycle()
        }
    }

    var stubNoConnectionMessage: String = ""
        set(value) {
            field = value
            noConnectionMessage.text = field
        }

    var stubRetryButton: String = ""
        set(value) {
            field = value
            if (field.isNotBlank()) {
                retryButton.makeVisible()
            }
        }

    @RawRes
    var stubAnimation: Int = R.raw.no_internet_connection_animation
        set(value) {
            field = value
            animationView.setAnimation(field)
        }

    fun setOnRepeatClickListener(listener: () -> Unit) {
        retryButton.setOnClickListener { listener() } }
}