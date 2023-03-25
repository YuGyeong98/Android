package com.example.client_server

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.example.client_server.databinding.ActivityMainBinding
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var serverHost = ""
        val client = OkHttpClient()

        binding.serverHostEditText.addTextChangedListener {
            serverHost = it.toString()
        }

        binding.connectButton.setOnClickListener {
            val request = Request.Builder()
                .url("http://$serverHost:8080")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "수신 실패", Toast.LENGTH_SHORT).show()
                    }
                    Log.e("CLIENT", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val res = response.body?.string()
                        runOnUiThread {
                            binding.connectTextView.apply {
                                isVisible = true
                                text = res
                            }
                            binding.serverHostEditText.isVisible = false
                            binding.connectButton.isVisible = false
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "수신 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
    }
}