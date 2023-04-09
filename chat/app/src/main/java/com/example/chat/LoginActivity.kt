package com.example.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chat.DBKey.Companion.DB_FCM_TOKEN
import com.example.chat.DBKey.Companion.DB_USERS
import com.example.chat.DBKey.Companion.DB_USER_ID
import com.example.chat.DBKey.Companion.DB_USER_NAME
import com.example.chat.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            Firebase.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    val currentUser = Firebase.auth.currentUser
                    if (task.isSuccessful && currentUser != null) {
                        FirebaseMessaging.getInstance().token.addOnCompleteListener {
                            val userToken = it.result
                            val userId = currentUser.uid
                            val userUpdates = hashMapOf<String, Any>(
                                DB_USER_ID to userId,
                                DB_USER_NAME to email,
                                DB_FCM_TOKEN to userToken
                            )
                            Firebase.database.reference.child(DB_USERS).child(userId).updateChildren(userUpdates)

                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                        Log.e("SIGN_UP", task.exception.toString())
                    }
                }
        }
        binding.signInButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                        Log.e("SIGN_IN", task.exception.toString())
                    }
                }
        }
    }
}