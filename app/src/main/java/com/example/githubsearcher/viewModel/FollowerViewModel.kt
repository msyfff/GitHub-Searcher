package com.example.githubsearcher.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubsearcher.response.ItemsItem
import com.example.githubsearcher.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerViewModel : ViewModel() {
    private val _apiData = MutableLiveData<List<ItemsItem>>()
    val apiData: LiveData<List<ItemsItem>> = _apiData

    private val _showError: MutableLiveData<Boolean> = MutableLiveData(false)
    val showError: LiveData<Boolean> = _showError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _showFailed: MutableLiveData<Boolean> = MutableLiveData(false)
    val showFailed: LiveData<Boolean> = _showFailed

    fun getFollower(users: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(users)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody!!.isNotEmpty()) {
                    _apiData.value = responseBody!!
                } else {
                    _showError.value = true
                }
            }


            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                _showFailed.value = true
            }

        })
    }
}