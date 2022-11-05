package com.android.wearther.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BaseViewModel: ViewModel() {
    fun<T> requestService(
        callRequest : Call<T>,
        onFailed: ((Throwable) -> Unit)? = null,
        onSuccess: (T) -> Unit
    ) {
        callRequest.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                Log.i("WEATHER", "onResponse(): $response")
                if (response.code() == 200){
                    val responseBody = response.body()
                    Log.i("WEATHER", "response.body : ${response.body()}")
                    if (responseBody != null) {
                        onSuccess.invoke(responseBody)
                    } else {
                        Log.e("WEATHER", "Response was null ${response.message()}")
                        onFailed?.invoke(Throwable("Response was null ${response.message()}"))
                    }
                } else {
                    Log.e("WEATHER", "Code : ${response.code()} / ${response.message()}")
                    onFailed?.invoke(Throwable("${response.code()} / ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                Log.e("WEATHER", t.localizedMessage ?: "Unknown Error")
                onFailed?.invoke(t)
            }

        })

    }
}