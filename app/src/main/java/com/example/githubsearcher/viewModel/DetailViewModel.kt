package com.example.githubsearcher.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubsearcher.response.DetailResponse
import com.example.githubsearcher.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback

class DetailViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _setDetail = MutableLiveData<DetailResponse>()
    val setDetail: LiveData<DetailResponse> = _setDetail

    private val detailUser = MutableLiveData<DetailResponse>()

    companion object {
        const val TAG = "DetailActivity"
    }

    fun detailApi(user: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetail(user)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: retrofit2.Response<DetailResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful) {
                    _setDetail.value = responseBody!!
                } else {
                    Log.e(TAG, "onResponse: ${response.message()}")
                }
            }


            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getDetail(): LiveData<DetailResponse> = detailUser

}