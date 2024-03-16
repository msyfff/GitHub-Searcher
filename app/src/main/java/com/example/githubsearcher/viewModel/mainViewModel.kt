package com.example.githubsearcher.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubsearcher.response.ItemsItem
import com.example.githubsearcher.response.Response
import com.example.githubsearcher.retrofit.ApiConfig
import com.example.githubsearcher.util.Event
import retrofit2.Call
import retrofit2.Callback

class MainViewModel : ViewModel() {

    private val _apiData = MutableLiveData<List<ItemsItem>>()
    val apiData: LiveData<List<ItemsItem>> = _apiData

    private val _showError: MutableLiveData<Boolean> = MutableLiveData(false)
    val showError: LiveData<Boolean> = _showError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _showFailed: MutableLiveData<Boolean> = MutableLiveData(false)
    val showFailed: LiveData<Boolean> = _showFailed

    private val _showDefault: MutableLiveData<Boolean> = MutableLiveData(true)
    val showDefault: LiveData<Boolean> = _showDefault

    private val _snackBar = MutableLiveData<Event<Int>>()
    val snackBar : LiveData<Event<Int>> = _snackBar

    companion object {
        const val TAG = "MainActivity"
    }

    fun findApi(query : String) {
        _isLoading.value = true
        _showDefault.value = false
        _showFailed.value = false
        _showError.value = false
        val client = ApiConfig.getApiService().getApi(query)
        client.enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                val responseBody = response.body()
                _isLoading.value = false
                _showDefault.value = false
                _showFailed.value = false
                _showError.value = false
                if (response.isSuccessful && responseBody?.items!!.isNotEmpty()) {
                    _apiData.value = responseBody.items
                    _snackBar.value = Event(responseBody.totalCount)
                } else {
                    _showError.value = true
                    _apiData.value = responseBody?.items
                    Log.e(TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                _isLoading.value = false
                _showFailed.value = true
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}