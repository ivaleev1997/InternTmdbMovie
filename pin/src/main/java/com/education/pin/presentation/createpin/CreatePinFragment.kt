package com.education.pin.presentation.createpin

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.education.core_api.extension.makeInvisible
import com.education.core_api.extension.makeVisible
import com.education.core_api.extension.observe
import com.education.core_api.presentation.fragment.BaseFragment
import com.education.pin.R
import com.education.pin.domain.entity.EnterKeyStatus
import com.education.pin.domain.entity.Number
import com.education.pin.domain.entity.PinViewState
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.create_pin_fragment.*
import kotlinx.android.synthetic.main.hidden_display.*
import kotlinx.android.synthetic.main.pin_keyboard.*

class CreatePinFragment : BaseFragment(R.layout.create_pin_fragment) {

    companion object {
        fun newInstance() =
            CreatePinFragment()
    }

    private val groupAdapter = GroupAdapter<ViewHolder>().apply {
        spanCount = 3
    }

    private val viewModel: CreatePinViewModel by viewModels()

    override fun initViewElements(view: View) {
        setupToolBar()

        pinRecyclerView.apply {
            layoutManager = GridLayoutManager(context, groupAdapter.spanCount)
            adapter = groupAdapter
        }
        groupAdapter.update(viewModel.genKeyboardItems(9))

        observe(viewModel.liveState, ::renderViewState)
    }

    private fun renderViewState(pinViewState: PinViewState) {
        when(pinViewState.enterKeyStatus) {
            EnterKeyStatus.ENTER -> {
                when(pinViewState.number) {
                    Number.FIRST -> {
                        imageView1.setImageDrawable(resources.getDrawable(R.drawable.ic_number_pressed))
                    }
                    Number.SECOND -> {
                        imageView2.setImageDrawable(resources.getDrawable(R.drawable.ic_number_pressed))
                    }
                    Number.THIRD -> {
                        imageView3.setImageDrawable(resources.getDrawable(R.drawable.ic_number_pressed))
                    }
                    Number.FOURTH -> {
                        imageView4.setImageDrawable(resources.getDrawable(R.drawable.ic_number_pressed))
                    }
                }
            }
            EnterKeyStatus.BACKSPACE -> {
                wrongEnterTextView.makeInvisible()
                    when(pinViewState.number) {
                    Number.FIRST -> {
                        imageView1.setImageDrawable(resources.getDrawable(R.drawable.ic_number_not_pressed))
                    }
                    Number.SECOND -> {
                        imageView2.setImageDrawable(resources.getDrawable(R.drawable.ic_number_not_pressed))
                    }
                    Number.THIRD -> {
                        imageView3.setImageDrawable(resources.getDrawable(R.drawable.ic_number_not_pressed))
                    }
                    Number.FOURTH -> {
                        imageView4.setImageDrawable(resources.getDrawable(R.drawable.ic_number_not_pressed))
                    }
                }
            }
            EnterKeyStatus.REPEAT -> {
                createPinTitleTextView.makeInvisible()
                createPinTitleTextView.setText(R.string.repeat_pin_code)
                createPinTitleTextView.makeVisible()
                setAllHidePinIconsNotPressed()
            }
            EnterKeyStatus.CLEAN -> {
                createPinTitleTextView.makeInvisible()
                createPinTitleTextView.setText(R.string.come_up_with_pin_code)
                createPinTitleTextView.makeVisible()
                wrongEnterTextView.makeInvisible()
                setAllHidePinIconsNotPressed()
            }
            EnterKeyStatus.ERROR -> {
                wrongEnterTextView.makeVisible()
                setAllHidePinIconsError()
            }
        }
    }

    private fun setAllHidePinIconsNotPressed() {
        imageView1.setImageDrawable(resources.getDrawable(R.drawable.ic_number_not_pressed))
        imageView2.setImageDrawable(resources.getDrawable(R.drawable.ic_number_not_pressed))
        imageView3.setImageDrawable(resources.getDrawable(R.drawable.ic_number_not_pressed))
        imageView4.setImageDrawable(resources.getDrawable(R.drawable.ic_number_not_pressed))
    }

    private fun setAllHidePinIconsError() {
        imageView1.setImageDrawable(resources.getDrawable(R.drawable.ic_number_wrong))
        imageView2.setImageDrawable(resources.getDrawable(R.drawable.ic_number_wrong))
        imageView3.setImageDrawable(resources.getDrawable(R.drawable.ic_number_wrong))
        imageView4.setImageDrawable(resources.getDrawable(R.drawable.ic_number_wrong))
    }

    private fun setupToolBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).title = " "

        toolbar.setNavigationOnClickListener {
            if (!viewModel.isSecondDeque)
                navigateUp()
            else
                viewModel.onBackNavigatePressed()
        }

    }
}
