package com.education.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.education.login.dto.LoginResult
import com.education.login.dto.ValidationStatus
import com.education.login.viewmodel.LoginViewModel
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var enterBtn: Button
    private lateinit var loginTextInput: TextInputLayout
    private lateinit var passwdTextInput: TextInputLayout
    private lateinit var enterStatusTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewElements(view)
    }

    private fun initViewElements(view: View) {
        view.setOnClickListener {
            hideKeyboard()
        }

        // меняю бэк неактивного Button программно при инициализации, а не через xml,
        // так как в библиотеке material есть баг с заливкой бэк'а
        // через xml (https://github.com/material-components/material-components-android/issues/889)
        enterBtn = view.findViewById(R.id.enterButton)
        disableEnterButton()

        loginTextInput = view.findViewById(R.id.loginTextInput)
        passwdTextInput = view.findViewById(R.id.passwdTextInput)
        enterStatusTextView = view.findViewById(R.id.enterStatusTextView)

        enterBtn.setOnClickListener {
            viewModel.login(
                loginTextInput.getEditTextString(),
                passwdTextInput.getEditTextString()
            )
        }

        loginTextInput.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus)
                validateLogin()
        }

        passwdTextInput.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus)
                validatePasswd()
        }

        passwdTextInput.editText?.setOnEditorActionListener { _, actionId, event ->
            Log.d("LOGIN_FRAGMENT", "keyboard $actionId $event")
            hideKeyboard()
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                passwdTextInput.editText?.clearFocus()
            }

            false
        }

        viewModel.validateStatus.observe(viewLifecycleOwner) { status ->
            when (status.first) {
                ValidationStatus.LOGIN -> {
                    loginTextInput.error =
                        if (status.second)
                            null
                        else {
                            disableEnterButton()
                            resources.getString(R.string.incorrect_login)
                        }
                }
                ValidationStatus.PASSWD -> {
                    passwdTextInput.error =
                        if (status.second)
                            null
                        else {
                            disableEnterButton()
                            resources.getString(R.string.incorrect_passwd)
                        }
                }
                ValidationStatus.SUCCESS -> enableEnterButton()

                else -> {}
            }
        }

        viewModel.login.observe(viewLifecycleOwner) { resultStatus ->
            when (resultStatus) {
                LoginResult.LOGIN_OR_PASSWD -> setMsgToEnterStatusTextView(R.string.error_login)
                LoginResult.TRY_LATER -> setMsgToEnterStatusTextView(R.string.error_later)
                LoginResult.SUCCESS -> {
                    setEnterStatusTextViewGone()
                    // Старт следующего события
                }
            }
        }
    }

    private fun setMsgToEnterStatusTextView(stringId: Int) {
        enterStatusTextView.setText(stringId)
        enterStatusTextView.visibility = View.VISIBLE
    }

    private fun setEnterStatusTextViewGone() {
        enterStatusTextView.visibility = View.GONE
    }

    private fun enableEnterButton() {
        enterBtn.setBackgroundColor(resources.getColor(R.color.enabled_fill_button_color))
        enterBtn.setTextColor(resources.getColor(R.color.text_color))
        enterBtn.isEnabled = true
    }

    private fun disableEnterButton() {
        enterBtn.setBackgroundColor(resources.getColor(R.color.disabled_fill_button_color))
        enterBtn.setTextColor(resources.getColor(R.color.gray))
        enterBtn.isEnabled = false
    }

    private fun hideKeyboard() {
        val view = activity?.currentFocus
        if (view != null) {
            val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }
        if (loginTextInput.editText?.isFocused == true) validateLogin()
        if (passwdTextInput.editText?.isFocused == true) validatePasswd()
    }

    private fun validateLogin() {
        viewModel.isLoginValid(loginTextInput.getEditTextString())
    }

    private fun validatePasswd() {
        viewModel.isPasswdValid(passwdTextInput.getEditTextString())
    }

    private fun TextInputLayout.getEditTextString(): String = this.editText?.text.toString()
}
