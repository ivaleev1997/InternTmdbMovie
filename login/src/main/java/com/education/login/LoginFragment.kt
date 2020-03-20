package com.education.login

import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.education.core_api.extension.getEditTextString
import com.education.core_api.fragment.BaseFragment
import com.education.core_api.mediator.AppWithComponent
import com.education.core_api.viewmodel.ViewModelTrigger
import com.education.login.di.LoginComponent
import com.education.login.dto.LoginResult
import com.education.login.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.login_fragment.*
import javax.inject.Inject

class LoginFragment : BaseFragment(R.layout.login_fragment) {

    companion object {
        fun newInstance() = LoginFragment()
    }

    @Inject
    lateinit var appViewModelFactory: ViewModelProvider.Factory
    private val viewModel: LoginViewModel by viewModels {
        appViewModelFactory
    }

    @Inject
    lateinit var viewModelTrigger: ViewModelTrigger

    override fun onAttach(context: Context) {
        LoginComponent.create((requireActivity().application as AppWithComponent).getComponent()).inject(this)
        super.onAttach(context)
    }

    override fun initViewElements(view: View) {
        view.setOnClickListener {
            hideKeyboard()
            checkFocus()
        }

        enterButton.setOnClickListener {
            viewModel.onLoginClicked(
                loginTextInput.getEditTextString(),
                passwordTextInput.getEditTextString()
            )
        }

        loginTextInput.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus)
                validateLogin()
        }

        passwordTextInput.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus)
                validatePassword()
        }

        passwordTextInput.editText?.setOnEditorActionListener { _, actionId, event ->
            Log.d("LOGIN_FRAGMENT", "keyboard $actionId $event")
            hideKeyboard()
            checkFocus()
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                passwordTextInput.editText?.clearFocus()
            }

            false
        }

        viewModel.validateLoginStatus.observe(viewLifecycleOwner) { validateLogin ->
            loginTextInput.error =
                if (validateLogin)
                    null
                else {
                    resources.getString(R.string.incorrect_login)
                }
        }

        viewModel.validateButtonStatus.observe(viewLifecycleOwner) { buttonEnabled ->
            if (buttonEnabled)
                enableEnterButton()
            else
                disableEnterButton()
        }

        viewModel.validatePasswordStatus.observe(viewLifecycleOwner) { validatePassword ->
            passwordTextInput.error =
                if (validatePassword)
                    null
                else {
                    resources.getString(R.string.incorrect_passwd)
                }
        }

        viewModel.login.observe(viewLifecycleOwner) { resultStatus ->
            when (resultStatus) {
                LoginResult.LOGIN_OR_PASSWORD -> setMsgToEnterStatusTextView(R.string.error_login)
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
        enterButton.setBackgroundColor(resources.getColor(R.color.enabled_fill_button_color))
        enterButton.setTextColor(resources.getColor(R.color.text_color))
        enterButton.isEnabled = true
    }

    private fun disableEnterButton() {
        enterButton.setBackgroundColor(resources.getColor(R.color.disabled_fill_button_color))
        enterButton.setTextColor(resources.getColor(R.color.gray))
        enterButton.isEnabled = false
    }

    private fun validateLogin() {
        viewModel.onLoginEntered(loginTextInput.getEditTextString())
    }

    private fun validatePassword() {
        viewModel.onPasswordEntered(passwordTextInput.getEditTextString())
    }

    private fun checkFocus() {
        if (loginTextInput.editText?.isFocused == true) validateLogin()
        if (passwordTextInput.editText?.isFocused == true) validatePassword()
    }
}
