package com.example.wsamad4.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.wsamad4.R
import com.example.wsamad4.core.Constants
import com.example.wsamad4.core.networkInfo
import com.example.wsamad4.data.get
import com.example.wsamad4.databinding.FragmentQrBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class QrFragment : Fragment(R.layout.fragment_qr) {
    private lateinit var binding: FragmentQrBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentQrBinding.bind(view)

        internetInfo()
        clicks()
    }

    private fun clicks() {
        binding.imgBack.setOnClickListener { findNavController().popBackStack()}
        binding.txtTryAgain.setOnClickListener { validateInternet() }
    }

    private fun validateInternet() {
        binding.llNoInternet.visibility = View.GONE
        internetInfo()
    }

    private fun internetInfo() {
        if (!networkInfo(requireContext())) {
            binding.llNoInternet.visibility = View.VISIBLE
        } else {
            binding.llWithInternet.visibility = View.VISIBLE
            obtainQR()
        }
    }

    private fun obtainQR() {
        Constants.okHttp.newCall(get("user_qr")).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onFailure: ", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val json = JSONTokener(response.body!!.string()).nextValue() as JSONObject
                val url = URL(json.getString("data")).openConnection() as HttpURLConnection
                val bitmap = BitmapFactory.decodeStream(url.inputStream)
                requireActivity().runOnUiThread {
                    binding.imgQr.setImageBitmap(bitmap)
                }
            }
        })
    }

}