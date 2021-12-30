package com.robbysalam.fatah.API

import android.content.Context

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.robbysalam.fatah.listeners.ResponseListener


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ApiRequests(private val context: Context, private val listener: ResponseListener) {
    /*
     Metode ini akan mengembalikan array Json untuk respons API. Api memiliki respon di JSON array
     metode yang di gunakan =
     */
    fun getApiRequestMethodarray(pageno: Int, action: String) {

        val apiInterface = ApiClient.apiClient!!.create(ApiInterface::class.java)


        val call = apiInterface.getApiRequestsArray(Constants.UNSPLASH_CLIENT_ID, pageno)
        call.enqueue(object : Callback<JsonArray> {


            override fun onResponse(call: Call<JsonArray>, resp: Response<JsonArray>) {

                /*
                     check respon
                     */
                if (resp != null && resp.body() != null) {
                    listener.onSuccess(resp.body()!!.toString(), action)
                } else {
                    listener.onError(resp.message())
                }

            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                t.printStackTrace()
                listener.onError(t.message ?: "")
            }
        })
    }


    /*

   memanggil API gambar yang berkaitan

   */
    fun getApiRelatedImage(id: String) {

        val apiInterface = ApiClient.apiClient!!.create(ApiInterface::class.java)

        val call = apiInterface.getPhotosById(id, Constants.UNSPLASH_CLIENT_ID)
        call.enqueue(object : Callback<JsonObject> {


            override fun onResponse(call: Call<JsonObject>, resp: Response<JsonObject>) {

                /*
                     check respos
                     */
                if (resp != null && resp.body() != null) {

                    listener.onSuccess(resp.body()!!.toString(), id)
                } else {
                    listener.onError(resp.message())
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                t.printStackTrace()
                listener.onError(t.message ?: "")
            }
        })
    }


}
