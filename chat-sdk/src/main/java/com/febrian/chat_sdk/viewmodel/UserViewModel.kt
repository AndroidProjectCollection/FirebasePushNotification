package com.febrian.chat_sdk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.febrian.chat_sdk.data.User
import com.febrian.chat_sdk.repository.UserRepository

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getFindUser(uid : String) : LiveData<ArrayList<User>> = userRepository.getFindUser(uid, _isLoading)

}