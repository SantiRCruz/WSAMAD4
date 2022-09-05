package com.example.wsamad4.data

import android.hardware.usb.UsbEndpoint
import com.example.wsamad4.core.Constants
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

fun signIn(email:String,password:String):RequestBody{
    val json = JSONObject().apply {
        put("login",email)
        put("password",password)
    }
    return json.toString().toRequestBody("application/json".toMediaType())
}

fun get(endpoint: String):Request{
    return Request.Builder().url("${Constants.url}/$endpoint").get().build()
}
fun post(endpoint: String,body: RequestBody):Request{
    return Request.Builder().url("${Constants.url}/$endpoint").post(body).build()
}