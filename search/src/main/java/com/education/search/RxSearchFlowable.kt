package com.education.search

import android.view.inputmethod.EditorInfo
import android.widget.EditText
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

class RxSearchFlowable {
    companion object {
        fun fromView(
            editText: EditText,
            hideKeyboard: () -> Unit
        ): Flowable<String> {
            return Flowable.create({ source ->
                editText.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        source.onNext(editText.text.toString())
                        hideKeyboard.invoke()
                        true
                    } else {
                        false
                    }
                }
                editText.setOnKeyListener { _, _, _ ->
                    source.onNext(editText.text.toString())
                    true
                }
            }, BackpressureStrategy.BUFFER)
        }
    }
}