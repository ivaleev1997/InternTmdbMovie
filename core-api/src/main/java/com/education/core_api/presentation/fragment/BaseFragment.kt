package com.education.core_api.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.education.core_api.R
import com.education.core_api.presentation.activity.BaseActivity
import com.education.core_api.presentation.uievent.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import timber.log.Timber

abstract class BaseFragment(private val layoutId: Int) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewElements(view)
    }

    protected abstract fun initViewElements(view: View)

    open fun hideKeyboard() {
        val view = activity?.currentFocus
        if (view != null) {
            val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showNoNetworkSnackBar(rootView: View, anchorView: View?) {
        showSnackBar(rootView, resources.getString(R.string.no_internet), anchorView)
    }

    private fun showSnackBar(view: View, message: String, anchorView: View?) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAnchorView(anchorView).show()
    }

    fun TextInputLayout.getEditTextString(): String = this.editText?.text.toString()

    fun logout() {
        (requireActivity() as BaseActivity).logout()
    }


    fun navigateUp() {
        Timber.d("Navigate up")
        findNavController().navigateUp()
    }

    protected fun onFragmentEvent(event: Event, view: View, anchorView: View? = null) {
        when (event) {
            is NoNetworkEvent -> {
                showNoNetworkSnackBar(view, anchorView)
            }
            is UnAuthorizedEvent -> {
                logout()
            }
            is TryLaterEvent -> {
                showTryLaterSnackBar(view, anchorView)
            }

            is LogoutEvent -> logout()

            is NavigateToEvent -> {
                navigateTo(event.navDirections)
            }
        }
    }

    private fun navigateTo(navDirections: NavDirections) {
        findNavController().navigate(navDirections)
    }

    private fun showTryLaterSnackBar(rootView: View, anchorView: View?) {
        showSnackBar(rootView, resources.getString(R.string.try_later), anchorView)
    }
}
