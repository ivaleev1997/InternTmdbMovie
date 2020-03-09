package com.education.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewElements(view)
    }

    private fun initViewElements(view: View) {
        enterBtn = view.findViewById(R.id.enterButton)
        enterBtn.setBackgroundColor(resources.getColor(R.color.fill_button_color))
        loginTextInput = view.findViewById(R.id.loginTextInput)
        passwdTextInput = view.findViewById(R.id.passwdTextInput)
    }
}
