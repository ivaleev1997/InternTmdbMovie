package com.education.core_api.extension

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.getEditTextString(): String = this.editText?.text.toString()