package com.febrian.chat_sdk.viewmodel

import androidx.lifecycle.*
import com.febrian.chat_sdk.response.AuthResponse
import com.febrian.chat_sdk.data.User
import com.febrian.chat_sdk.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _response = MutableLiveData<AuthResponse>()
    val response: LiveData<AuthResponse> = _response

    fun register(user: User) {
        viewModelScope.launch {
            repository.register(user, _isLoading, _response)
        }
    }

    fun login(user : User){
        viewModelScope.launch {
            repository.login(user, _isLoading, _response)
        }
    }

    fun getUpdateUser() : LiveData<User> = repository.getUpdateUser()

}