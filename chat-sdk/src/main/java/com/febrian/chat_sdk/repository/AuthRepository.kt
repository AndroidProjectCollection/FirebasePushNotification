package com.febrian.chat_sdk.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.febrian.chat_sdk.response.AuthResponse
import com.febrian.chat_sdk.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AuthRepository(private val activity: Activity) {

    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var user = MutableLiveData<User>()

    suspend fun register(
        user: User,
        isLoading: MutableLiveData<Boolean>,
        response: MutableLiveData<AuthResponse>
    ) {

        isLoading.value = true

        auth.createUserWithEmailAndPassword(
            user.email.toString(),
            user.password.toString()
        ).addOnCompleteListener(activity) { task ->
            isLoading.value = false

            if (task.isSuccessful) {
                response.value = AuthResponse(true, "Success")

                task.result.user?.let {
                    user.uid = it.uid
                    onAuthSuccess(user)
                }

            } else {
                response.value =
                    AuthResponse(false, "Sign Up Failed ${task.exception?.message.toString()}")
            }
        }
    }

    suspend fun login(
        user: User,
        isLoading: MutableLiveData<Boolean>,
        response: MutableLiveData<AuthResponse>
    ) {

        isLoading.value = true

        auth.signInWithEmailAndPassword(
            user.email.toString(),
            user.password.toString()
        ).addOnCompleteListener(
            activity
        ) { task ->

            isLoading.value = false

            if (task.isSuccessful) {

                response.value = AuthResponse(true, "Success")
                task.result.user?.let {
                    user.uid = it.uid
                    onAuthSuccess(user)
                }
            } else {
                response.value =
                    AuthResponse(false, "Login Failed ${task.exception?.message.toString()}")
            }
        }
    }

    private fun onAuthSuccess(user: User) {
        this.user.value = user
        database.child("users").child(user.uid.toString()).setValue(user)
    }

    fun getUpdateUser() : LiveData<User>{
        return user
    }

}