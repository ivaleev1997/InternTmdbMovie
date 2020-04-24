package com.education.core_api.presentation.view.zeroscreen

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.RawRes
import com.education.core_api.R
import com.education.core_api.extension.makeVisible
import kotlinx.android.synthetic.main.zero_screen_layout.view.*

class ZeroScreenWithAnimation @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.zero_screen_layout, this, true)

        lateinit var attrsArray: TypedArray

        try {
            attrsArray = context.obtainStyledAttributes(attrs, R.styleable.ZeroScreenView)
            with(attrsArray) {
                stubAnimation = getResourceId(
                    R.styleable.ZeroScreenView_animationView,
                    R.raw.no_internet_connection_animation
                )
                stubMessage = getString(R.styleable.ZeroScreenView_message) ?: resources.getString(
                    R.string.unexpected_network_error
                )
                stubRetryButton = getBoolean(R.styleable.ZeroScreenView_retryButton, false)
                stubBackground = getColor(R.styleable.ZeroScreenView_backgroundColor, 0)
            }
        } finally {
            attrsArray.recycle()
        }
    }

    var stubMessage: String = ""
        set(value) {
            field = value
            noConnectionMessage.text = field
        }

    var stubRetryButton: Boolean = false
        set(value) {
            field = value
            if (field) {
                retryButton.makeVisible()
            }
        }

    var stubBackground: Int = 0
        set(value) {
            field = value
            if (field != 0) {
                // Не получилось получить colorPrimary из темы программно
                zeroScreenContainer.setBackgroundColor(field)
            }
        }

    @RawRes
    var stubAnimation: Int = R.raw.no_internet_connection_animation
        set(value) {
            field = value
            animationView.setAnimation(field)
        }

    fun setOnRepeatClickListener(listener: () -> Unit) {
        retryButton.setOnClickListener { listener() }
    }
}