package com.febrian.firebasenotification.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.febrian.chat_sdk.data.User
import com.febrian.chat_sdk.service.MessageService
import com.febrian.chat_sdk.viewmodel.AuthViewModel
import com.febrian.chat_sdk.utils.Constant
import com.febrian.chat_sdk.utils.PreferenceManager
import com.febrian.chat_sdk.utils.ViewModelFactory
import com.febrian.firebasenotification.databinding.ActivityAuthBinding
import com.febrian.firebasenotification.ui.TOPIC
import com.google.firebase.messaging.FirebaseMessaging

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(applicationContext)

        if (preferenceManager.getString(Constant.UID).isNotEmpty()) {
            goFindUserActivity()
        }

        val authViewModel = ViewModelFactory(this).create(AuthViewModel::class.java)

        initializeNotification()

        binding.btnCreate.setOnClickListener {

            if (!validateForm()) return@setOnClickListener
            val user = getUser()

            authViewModel.register(user)
        }

        binding.btnLogin.setOnClickListener {
            if (!validateForm()) return@setOnClickListener
            val user = getUser()

            authViewModel.login(user)
        }

        authViewModel.isLoading.observe(this) {
            when (it) {
                true -> {
                    binding.loading.visibility = View.VISIBLE
                }
                false -> {
                    binding.loading.visibility = View.GONE
                }
            }
        }

        authViewModel.response.observe(this) {
            when (it.success) {
                true -> {
                    showToast(it.message)
                    authViewModel.getUpdateUser().observe(this) { user ->
                        saveData(user)
                    }
                    goFindUserActivity()
                }
                false -> {
                    showToast(it.message)
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun getUser(): User {
        return User(
            email = binding.edtEmail.text.toString(),
            username = binding.edtEmail.text.toString().let { usernameFromEmail(it) },
            password = binding.edtPassword.text.toString(),
            token = MessageService.token
        )
    }

    private fun initializeNotification() {
        MessageService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            MessageService.token = it
        }
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
    }

    private fun usernameFromEmail(email: String): String {
        return if (email.contains("@")) {
            email.split("@").toTypedArray()[0]
        } else {
            email
        }
    }

    private fun goFindUserActivity() {
        val intent = Intent(this@AuthActivity, FindUserActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateForm(): Boolean {
        var result = true

        if (TextUtils.isEmpty(binding.edtEmail.text.toString())) {
            binding.edtEmail.error = "Required"
            result = false
        } else {
            binding.edtEmail.error = null
        }
        if (TextUtils.isEmpty(binding.edtPassword.text.toString())) {
            binding.edtPassword.error = "Required"
            result = false
        } else {
            binding.edtPassword.error = null
        }
        return result
    }

    private fun saveData(user: User) {
        preferenceManager.putString(Constant.UID, user.uid.toString())
        preferenceManager.putString(Constant.USERNAME, user.username.toString())
        preferenceManager.putString(Constant.TOKEN, user.token.toString())
    }

    companion object {
        const val TAG = "Auth Activity"
    }
}