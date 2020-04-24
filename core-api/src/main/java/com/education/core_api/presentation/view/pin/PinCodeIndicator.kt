package com.education.core_api.presentation.view.pin

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import com.education.core_api.R
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.roundToInt


class PinCodeIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val MIN_INDICATORS_COUNT_4 = 4
        private const val FIRST_INDEX_0 = 0
        private const val DEFAULT_MARGIN_START_IMAGES_8 = 8
    }

    private var parentContainerView: LinearLayout =
        LayoutInflater.from(context).inflate(R.layout.pin_indicator_layout, this, true) as LinearLayout

    private var mapImageValues = HashMap<Int, ImageView>()
    private var dequeDisplayedImages = LinkedList<Int>()
    private var currentFromDisplayedIndex = FIRST_INDEX_0
    private var dequeCurrentEnterImages = LinkedList<Int>()

    private var stubDefaultIndicatorResourceImage: Drawable = resources.getDrawable(R.drawable.ic_number_not_pressed)

    private var stubEnterIndicatorResourceImage: Drawable = resources.getDrawable(R.drawable.ic_number_pressed)

    private var stubErrorIndicatorResourceImage: Drawable = resources.getDrawable(R.drawable.ic_number_wrong)

    init {

        lateinit var attrsArray: TypedArray

        try {
            attrsArray = context.obtainStyledAttributes(attrs, R.styleable.PinCodeIndicator)
            with(attrsArray) {
                stubDefaultIndicatorResourceImage = getDrawable(R.styleable.PinCodeIndicator_defaultIndicatorDrawableResource)
                    ?: resources.getDrawable(R.drawable.ic_number_not_pressed)
                stubEnterIndicatorResourceImage = getDrawable(R.styleable.PinCodeIndicator_enterIndicatorDrawableResource)
                    ?: resources.getDrawable(R.drawable.ic_number_pressed)
                stubErrorIndicatorResourceImage = getDrawable(R.styleable.PinCodeIndicator_errorIndicatorDrawableResource)
                    ?: resources.getDrawable(R.drawable.ic_number_wrong)

                stubIndicatorsCount = getInteger(
                    R.styleable.PinCodeIndicator_countIndicators,
                    MIN_INDICATORS_COUNT_4
                )
            }
        } finally {
            attrsArray.recycle()
        }
    }

    private var stubIndicatorsCount: Int = MIN_INDICATORS_COUNT_4
        set(value) {
            field = value
            for(i in (FIRST_INDEX_0 until field)) {
                mapImageValues[i] = ImageView(context).apply {
                    val imageViewLayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                    if (i > FIRST_INDEX_0) {
                        imageViewLayoutParams.marginStart = dpToPx(DEFAULT_MARGIN_START_IMAGES_8)
                    }
                    layoutParams = imageViewLayoutParams
                    setImageDrawable(stubDefaultIndicatorResourceImage)
                    parentContainerView.addView(this)
                    dequeDisplayedImages.add(i)
                }
            }
        }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).roundToInt()
    }

    fun itemPressed() {
        if (currentFromDisplayedIndex < stubIndicatorsCount) {
            mapImageValues[dequeDisplayedImages[currentFromDisplayedIndex]]?.setImageDrawable(stubEnterIndicatorResourceImage)
            dequeCurrentEnterImages.add(dequeDisplayedImages[currentFromDisplayedIndex++])
        }
    }

    fun clear() {
        dequeCurrentEnterImages.clear()
        currentFromDisplayedIndex = FIRST_INDEX_0
        dequeDisplayedImages.forEach {
            mapImageValues[it]?.setImageDrawable(stubDefaultIndicatorResourceImage)
        }
    }

    fun wrongEntered() {
        dequeDisplayedImages.forEach {
            mapImageValues[it]?.setImageDrawable(stubErrorIndicatorResourceImage)
        }
    }

    fun backspacePressed() {
        if (currentFromDisplayedIndex > FIRST_INDEX_0) {
            mapImageValues[dequeDisplayedImages[--currentFromDisplayedIndex]]?.setImageDrawable(stubDefaultIndicatorResourceImage)
            dequeCurrentEnterImages.removeLast()
        }
    }
}