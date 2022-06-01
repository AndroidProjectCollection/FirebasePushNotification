package com.febrian.chat_sdk.repository

import android.util.Log
import com.febrian.chat_sdk.api.ApiConfig
import com.febrian.chat_sdk.data.Chat
import com.febrian.chat_sdk.data.MessageData
import com.febrian.chat_sdk.data.PushNotification
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatRepository {

    private var  database = FirebaseDatabase.getInstance().reference

    suspend fun addMessage(rootKey : String, chat : Chat){
        database.child("chats").child(rootKey).setValue(chat)
        PushNotification(
            MessageData(chat.sender, chat.message),
            chat.token
        ).also {
            sendNotification(it)
        }
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = ApiConfig.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d("Notification", "Response: ${response}")
            } else {
                Log.e("Notification", response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.e("Notification", e.toString())
        }
    }
}