package com.example.wsamad4

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.example.wsamad4.core.Constants
import com.example.wsamad4.core.networkInfo
import com.example.wsamad4.data.post
import com.example.wsamad4.data.signIn
import com.example.wsamad4.databinding.ActivityLoginBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.supportActionBar?.hide()

        clicks()
    }
    private fun clicks() {
        binding.btnSignInLogin.setOnClickListener { validate() }
    }

    private fun validate() {
        val results = arrayOf(validateEmail(), validatePassword())

        if (false in results) {
            return
        }

        if (!networkInfo(applicationContext)) {
            return
        }
        sendVisible(true)
        sendSignIn()
    }

    private fun sendSignIn() {
        Constants.okHttp.newCall(
            post(
                "signin",
                signIn(binding.edtLogin.text.toString(), binding.edtPassword.text.toString())
            )
        ).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onFailure: ",e.message.toString())
                Log.e("onFailure: ",e.localizedMessage.toString())
                Log.e("onFailure: ",e.stackTrace.contentToString())
                Log.e("onFailure: ",e.cause.toString())
                Log.e("onFailure: ",e.suppressed.contentToString())
                runOnUiThread {
                    sendVisible(false)
                    alertMessage("Error from Server")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                Log.e("onResponse: ",response.message.toString())
                val json = JSONTokener(response.body!!.string()).nextValue() as  JSONObject
                if (json.getBoolean("success")){
                    val data = json.getJSONObject("data")
                    val sharedPreferences = getSharedPreferences(Constants.USER,Context.MODE_PRIVATE)
                    with(sharedPreferences.edit()){
                        putString("id",data.getString("id"))
                        putString("name",data.getString("name"))
                        putString("token",data.getString("token"))
                        apply()
                    }
                    runOnUiThread {
                        sendVisible(false)
                    }

                    Handler.createAsync(Looper.getMainLooper()).postDelayed({
                        val i = Intent(this@LoginActivity,HomeActivity::class.java)
                        startActivity(i)
                    },300)
                }else{
                    runOnUiThread {
                        sendVisible(false)
                        alertMessage("Wrong Credentials!")
                    }
                }
            }
        })
    }

    private fun sendVisible(b: Boolean) {
        if (b) {
            binding.progress.visibility = View.VISIBLE
            binding.btnSignInLogin.visibility = View.GONE
        } else {
            binding.progress.visibility = View.GONE
            binding.btnSignInLogin.visibility = View.VISIBLE
        }
    }

    private fun validateEmail(): Boolean {
        val regex = Pattern.compile("^([a-zA-Z]{1,}@wsa[.]com)")
        return if (binding.edtLogin.text.toString().isNullOrEmpty()) {
            alertMessage("Any field can't be empty")
            return false
        } else if (!regex.matcher(binding.edtLogin.text).matches()) {
            alertMessage("The Email Must have an email format")
            return false
        } else {
            true
        }
    }

    private fun alertMessage(s: String) {
        binding.llAlert.visibility = View.VISIBLE
        binding.txtAlert.text = s
        binding.btnSignInLogin.animate().translationY(300f).setDuration(200).withEndAction {
            binding.llAlert.animate().alpha(1f).setDuration(200).withEndAction {
                binding.llAlert.animate().alpha(1f).setDuration(800).withEndAction {
                    binding.llAlert.animate().alpha(0f).setDuration(200)
                    binding.btnSignInLogin.animate().translationY(0f).setDuration(200)
                    binding.llAlert.visibility = View.GONE
                }
            }
        }
    }

    private fun validatePassword(): Boolean {
        return if (binding.edtPassword.text.toString().isNullOrEmpty()) {
            alertMessage("Any field can't be empty")
            return false
        } else {
            true
        }
    }
}