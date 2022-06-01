package com.febrian.firebasenotification.ui.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.febrian.chat_sdk.data.Chat
import com.febrian.chat_sdk.data.User
import com.febrian.chat_sdk.viewmodel.ChatViewModel
import com.febrian.chat_sdk.utils.Constant
import com.febrian.chat_sdk.utils.ViewModelFactory
import com.febrian.firebasenotification.databinding.ActivityChatBinding
import com.febrian.firebasenotification.ui.TOPIC
import com.google.firebase.messaging.FirebaseMessaging

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(Constant.sharedPreferences, Context.MODE_PRIVATE)

        val chatViewModel = ViewModelFactory(this).create(ChatViewModel::class.java)

        // another user
        val user = intent.getParcelableExtra<User>(Constant.USER) as User

        // my user
        val uid = sharedPreferences.getString(Constant.UID, "").toString()
        val username = sharedPreferences.getString(Constant.USERNAME, "").toString()
        val token = sharedPreferences.getString(Constant.TOKEN, "").toString()

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        binding.send.setOnClickListener {
            val message = binding.message.text.toString()
            val chat = Chat(username, message, user.token.toString())
            val rootKey = uid + user.uid

            chatViewModel.addMessage(rootKey, chat)
        }
    }

    companion object{
        const val TAG = "ChatActivity"
    }
}