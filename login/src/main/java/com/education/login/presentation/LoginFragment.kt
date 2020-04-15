package com.education.login.presentation

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.education.core_api.di.AppWithComponent
import com.education.core_api.presentation.activity.BaseActivity
import com.education.core_api.presentation.fragment.BaseFragment
import com.education.core_api.presentation.viewmodel.ViewModelTrigger
import com.education.login.R
import com.education.login.di.LoginComponent
import com.education.login.domain.entity.LoginResult
import kotlinx.android.synthetic.main.login_fragment.*
import javax.inject.Inject

class LoginFragment : BaseFragment(R.layout.login_fragment) {

    companion object {
        fun newInstance() = LoginFragment()
    }

    @Inject
    internal lateinit var appViewModelFactory: ViewModelProvider.Factory
    private val viewModel: LoginViewModel by viewModels {
        appViewModelFactory
    }

    @Inject
    internal lateinit var viewModelTrigger: ViewModelTrigger

    override fun onAttach(context: Context) {
        LoginComponent.create((requireActivity().application as AppWithComponent).getComponent())
            .inject(this)
        super.onAttach(context)
    }

    override fun initViewElements(view: View) {
        view.setOnClickListener {
            hideKeyboard()
            checkFocus()
        }

        enterButton.setOnClickListener {
            clearMsgFromEnterStatusTextView()
            enterProgressBar.visibility = View.VISIBLE
            viewModel.onLoginButtonClicked(
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
                    disableEnterButton()
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
                    disableEnterButton()
                    resources.getString(R.string.incorrect_passwd)
                }
        }

        viewModel.login.observe(viewLifecycleOwner) { resultStatus ->
            enterProgressBar.visibility = View.GONE
            when (resultStatus) {
                LoginResult.LOGIN_OR_PASSWORD -> setMsgToEnterStatusTextView(R.string.error_login)
                LoginResult.TRY_LATER -> setMsgToEnterStatusTextView(R.string.error_later)
                LoginResult.NO_NETWORK_CONNECTION -> showNoNetworkSnackBar(loginConstraintLayout, null)
                LoginResult.SUCCESS -> {
                    setEnterStatusTextViewGone()
                    (requireActivity() as BaseActivity).startMainAppScreen()
                }
            }
        }
    }

    private fun setMsgToEnterStatusTextView(stringId: Int) {
        enterStatusTextView.setText(stringId)
        enterStatusTextView.visibility = View.VISIBLE
    }

    private fun clearMsgFromEnterStatusTextView() {
        enterStatusTextView.visibility = View.GONE
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
