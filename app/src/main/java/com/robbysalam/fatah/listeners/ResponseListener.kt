package com.robbysalam.fatah.listeners



interface ResponseListener {
    fun onSuccess(`object`: String, action: String)
    fun onError(message: String)
}
