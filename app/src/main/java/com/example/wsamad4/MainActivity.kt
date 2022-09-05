package com.example.wsamad4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wsamad4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.supportActionBar?.hide()

        animate()

    }

    private fun animate() {
        binding.imgLogo.animate().translationY(-300f).alpha(0f).scaleY(0f).scaleX(0f).setDuration(0).withEndAction {
        binding.imgName.animate().translationY(-100f).alpha(0f).setDuration(0)
            binding.imgLogo.animate().translationY(0f).alpha(1f).scaleY(1f).scaleX(1f).setDuration(300).withEndAction {
                binding.imgName.animate().translationY(0f).alpha(1f).setDuration(300).withEndAction {
                    val i = Intent(this@MainActivity,LoginActivity::class.java)
                    startActivity(i)
                    finish()
                }
            }
        }
    }
}