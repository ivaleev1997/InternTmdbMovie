package com.education.core_impl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class ViewModelFactoryProvider
@Inject constructor(private val creators: @JvmSuppressWildcards MutableMap<Class<out ViewModel>, ViewModel>)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val found = creators.entries.single {
            modelClass.isAssignableFrom(it.key)
        }

        return found.value as T
    }
}
