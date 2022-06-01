package com.febrian.chat_sdk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febrian.chat_sdk.data.Chat
import com.febrian.chat_sdk.repository.ChatRepository
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {

    fun addMessage(rootKey: String, chat : Chat) {
        viewModelScope.launch {
            repository.addMessage(rootKey, chat)
        }
    }

}