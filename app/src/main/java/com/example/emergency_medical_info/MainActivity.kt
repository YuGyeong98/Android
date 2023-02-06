package com.example.emergency_medical_info

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.example.emergency_medical_info.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editButton.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        getDataUiUpdate()
    }

    private fun getDataUiUpdate() {
        with(getSharedPreferences(USER_INFORMATION, MODE_PRIVATE)) {
            binding.nameValueTextView.text = getString(NAME, "미정")
            binding.birthdayValueTextView.text = getString(BIRTHDAY, "미정")
            binding.bloodTypeValueTextView.text = getString(BLOOD_TYPE, "미정")
            binding.emergencyContactValueTextView.text = getString(EMERGENCY_CONTACT, "미정")

            val precautions = getString(PRECAUTIONS, "")
            if (precautions.isNullOrEmpty()) {
                binding.precautionsTextView.isVisible = false
                binding.precautionsValueTextView.isVisible = false
            } else {
                binding.precautionsTextView.isVisible = true
                binding.precautionsValueTextView.isVisible = true
                binding.precautionsValueTextView.text = precautions
            }
        }
    }
}