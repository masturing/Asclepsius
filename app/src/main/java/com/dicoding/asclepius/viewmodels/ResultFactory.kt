package com.dicoding.asclepius.viewmodels

import androidx.lifecycle.ViewModelProvider

class ResultFactory(private val search: String?) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
        return ResultViewModel(search) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
}
}
