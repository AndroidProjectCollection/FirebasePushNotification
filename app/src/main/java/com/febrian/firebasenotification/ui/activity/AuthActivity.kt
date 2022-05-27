package com.febrian.firebasenotification.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.febrian.firebasenotification.MessageService
import com.febrian.firebasenotification.data.User
import com.febrian.firebasenotification.databinding.ActivityAuthBinding
import com.febrian.firebasenotification.ui.TOPIC
import com.febrian.firebasenotification.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(Constant.sharedPreferences, Context.MODE_PRIVATE)

        if (sharedPreferences.getString(Constant.UID, "").toString().isNotEmpty()) {
            goFindUserActivity()
        }

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        initializeNotification()

        binding.btnCreate.setOnClickListener {
            signUp()
        }
        binding.btnLogin.setOnClickListener {
            signIn()
        }

    }

    private fun initializeNotification() {
        MessageService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            MessageService.token = it
        }
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
    }

    private fun signUp() {

        if (!validateForm()) {
            return
        }

        auth.createUserWithEmailAndPassword(
            binding.edtEmail.text.toString(),
            binding.edtPassword.text.toString()
        ).addOnCompleteListener(this) { task ->
            Log.d(TAG, "createUser:onComplete:" + task.isSuccessful)
            //hideProgressDialog();
            if (task.isSuccessful) {
                task.result.user?.let { onAuthSuccess(it) }
            } else {
                Toast.makeText(
                    this@AuthActivity, "Sign Up Failed ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun signIn() {

        if (!validateForm()) {
            return
        }

        auth.signInWithEmailAndPassword(
            binding.edtEmail.text.toString(),
            binding.edtPassword.text.toString()
        )
            .addOnCompleteListener(
                this
            ) { task ->
                Log.d(TAG, "signIn:onComplete:" + task.isSuccessful)
                //hideProgressDialog();
                if (task.isSuccessful) {
                    task.result.user?.let { onAuthSuccess(it) }
                } else {
                    Toast.makeText(
                        this@AuthActivity, "Sign In Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun usernameFromEmail(email: String): String {
        return if (email.contains("@")) {
            email.split("@").toTypedArray()[0]
        } else {
            email
        }
    }

    private fun onAuthSuccess(user: FirebaseUser) {

        val newUser = User(
            user.uid,
            user.email?.let { usernameFromEmail(it) },
            user.email,
            binding.edtPassword.text.toString(),
            MessageService.token

        )
        database.child("users").child(user.uid).setValue(newUser)

        saveData(newUser)

        // Go to FindUserActivity
        goFindUserActivity()
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
        val editor = sharedPreferences.edit()
        editor.putString(Constant.UID, user.uid)
        editor.putString(Constant.USERNAME, user.username)
        editor.putString(Constant.TOKEN, user.token)
        editor.apply()
    }

    companion object {
        const val TAG = "Auth Activity"
    }
}