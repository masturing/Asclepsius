package com.dicoding.asclepius.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.datasource.News
import com.dicoding.asclepius.network.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ResultViewModel(private val search: String?): ViewModel() {
    private val _news = MutableLiveData<News?>()
    val news: LiveData<News?> = _news
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        viewModelScope.launch { getListNews() }
    }

    private suspend fun getListNews() {
        coroutineScope.launch {
            _isLoading.value = true
            val getNews = ApiConfig.getApiService().getArticleList(search = search ?: "")
            try {
                _isLoading.value = false
                _news.postValue(getNews)
            } catch (e: Exception){
                _isLoading.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}