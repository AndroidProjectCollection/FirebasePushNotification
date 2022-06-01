package com.febrian.chat_sdk.utils

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.febrian.chat_sdk.di.Injection
import com.febrian.chat_sdk.viewmodel.AuthViewModel
import com.febrian.chat_sdk.viewmodel.ChatViewModel
import com.febrian.chat_sdk.viewmodel.UserViewModel

class ViewModelFactory(private val activity: Activity) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(Injection.provideAuthRepository(activity)) as T
            }
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(Injection.provideUserRepository()) as T
            }
            modelClass.isAssignableFrom(ChatViewModel::class.java) -> {
                ChatViewModel(Injection.provideChatRepository()) as T
            }
            else -> {
                throw Throwable("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }
}