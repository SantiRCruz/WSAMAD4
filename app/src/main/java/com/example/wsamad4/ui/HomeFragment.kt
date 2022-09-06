package com.example.wsamad4.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.navigation.fragment.findNavController
import com.example.wsamad4.R
import com.example.wsamad4.core.Constants
import com.example.wsamad4.data.get
import com.example.wsamad4.data.models.History
import com.example.wsamad4.databinding.FragmentHomeBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private val history = mutableListOf<History>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        obtainActualDate()
        obtainCases()
        obtainHistory()
        clicks()
    }

    private fun clicks() {
        binding.imgQrH.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_qrFragment) }
        binding.imgMap.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_mapFragment) }
    }

    private fun obtainHistory() {
        val sharedPreferences =
            requireActivity().getSharedPreferences(Constants.USER, Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id", "")
        val name = sharedPreferences.getString("name", "")
        setNames(name)
        Constants.okHttp.newCall(get("symptoms_history?user_id=$id")).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onFailure: ", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val json = JSONTokener(response.body!!.string()).nextValue() as JSONObject
                if (json.getBoolean("success")) {
                    val data = json.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val item = data.getJSONObject(i)
                        history.add(
                            History(
                                SimpleDateFormat("yyyy-mm-dd HH:mm:ss").parse(
                                    item.getString(
                                        "date"
                                    )
                                ), item.getInt("probability_infection")
                            )
                        )
                    }
                    val finalData = history[data.length() - 1]
                    requireActivity().runOnUiThread {
                        if (finalData.probability_infection > 50) {
                            binding.txtTitleWithData.text = "CALL TO DOCTOR"
                            binding.llBgWithData.backgroundTintList = getColorStateList(requireContext(),R.color.dark_blue)
                            binding.textView8.text = "You may be infected with a virus"
                        } else {
                            binding.txtTitleWithData.text = "CLEAR"
                            binding.llBgWithData.backgroundTintList = getColorStateList(requireContext(),R.color.blue_200)
                            binding.textView8.text = "* Wear mask. Keep 2m distance. Wash hands."
                        }
                        binding.txtMonthDay.text = SimpleDateFormat("mm/dd").format(finalData.date)
                        binding.txtYearHour.text = SimpleDateFormat("/yyyy KK:mma").format(finalData.date)
                        binding.llWithData.visibility = View.VISIBLE
                        binding.llWithData1.visibility = View.VISIBLE
                    }
                } else {
                    requireActivity().runOnUiThread {
                        binding.llNoData.visibility = View.VISIBLE
                        binding.llNoData1.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun setNames(name: String?) {
        if (name == null) {
            binding.txtNameNoData.text = ""
            binding.txtNameWithData.text = ""
        } else {
            binding.txtNameNoData.text = name
            binding.txtNameWithData.text = name
        }

    }

    private fun obtainCases() {
        Constants.okHttp.newCall(get("cases")).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onFailure: ", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val json = JSONTokener(response.body!!.string()).nextValue() as JSONObject
                val data = 0..50
                val num = data.random()
                requireActivity().runOnUiThread {
                    if (num > 0) {
                        binding.txtNumCases.text = num.toString()
                        binding.txtNumCases1.text = num.toString()
                        binding.llBgCases.backgroundTintList =
                            getColorStateList(requireContext(), R.color.dark_blue)
                        binding.llBgCases1.backgroundTintList =
                            getColorStateList(requireContext(), R.color.dark_blue)
                    } else {
                        binding.txtNumCases.text = "No Case"
                        binding.txtNumCases1.text = "No Case"
                    }
                }
            }
        })
    }

    private fun obtainActualDate() {
        binding.txtActualDate.text = SimpleDateFormat("MMM dd,yyyy").format(Date())
    }
}