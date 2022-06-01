package com.febrian.chat_sdk.di

import android.app.Activity
import com.febrian.chat_sdk.repository.AuthRepository
import com.febrian.chat_sdk.repository.ChatRepository
import com.febrian.chat_sdk.repository.UserRepository

object Injection{
    fun provideAuthRepository(activity: Activity) : AuthRepository {
        return AuthRepository(activity)
    }

    fun provideUserRepository() : UserRepository{
        return UserRepository()
    }

    fun provideChatRepository() : ChatRepository{
        return ChatRepository()
    }
}