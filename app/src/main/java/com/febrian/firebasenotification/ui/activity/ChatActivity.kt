package com.febrian.firebasenotification.ui.activity

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.febrian.firebasenotification.data.NotificationData
import com.febrian.firebasenotification.api.NotificationService
import com.febrian.firebasenotification.data.PushNotification
import com.febrian.firebasenotification.data.Chat
import com.febrian.firebasenotification.data.User
import com.febrian.firebasenotification.databinding.ActivityChatBinding
import com.febrian.firebasenotification.ui.TOPIC
import com.febrian.firebasenotification.utils.Constant
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TAG = "ChatActivity"
class ChatActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChatBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(Constant.sharedPreferences, Context.MODE_PRIVATE)
        database = FirebaseDatabase.getInstance().reference

        // another user
        val user = intent.getParcelableExtra<User>(Constant.USER) as User

        // my user
        val uid = sharedPreferences.getString(Constant.UID, "").toString()
        val username = sharedPreferences.getString(Constant.USERNAME, "").toString()
        val token = sharedPreferences.getString(Constant.TOKEN, "").toString()

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        binding.send.setOnClickListener {
            val message = binding.message.text.toString()
            val chat = Chat(username, message, token)
            database.child("chats").child(uid+user.uid).setValue(chat)

            PushNotification(
                NotificationData(username, message),
                user.token.toString()
            ).also {
                sendNotification(it)
            }
        }
    }


    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = NotificationService.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d(TAG, "Response: ${response}")
            } else {
                Log.e(TAG, response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.e(TAG, e.toString())
        }
    }
}