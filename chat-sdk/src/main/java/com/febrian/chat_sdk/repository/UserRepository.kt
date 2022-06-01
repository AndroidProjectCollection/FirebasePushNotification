package com.febrian.chat_sdk.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.febrian.chat_sdk.data.User
import com.google.firebase.database.*

class UserRepository {

    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun getFindUser(userUid : String, _loading : MutableLiveData<Boolean>) : LiveData<ArrayList<User>>{

        val listUser = MutableLiveData<ArrayList<User>>()

        _loading.value = true
        database.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                _loading.value = false

                if (snapshot.exists()) {

                    val tempListUser = ArrayList<User>()

                    snapshot.children.forEach {
                        val anotherUid = it.child("uid").value.toString()
                        if (userUid == anotherUid) {
                            return@forEach
                        }
                        val name = it.child("username").value.toString()
                        val email = it.child("email").value.toString()
                        val password = it.child("password").value.toString()
                        val token = it.child("token").value.toString()

                        val user = User(anotherUid, name, email, password, token)
                        tempListUser.add(user)
                    }

                    listUser.value = tempListUser

                } else {
                    _loading.value = true
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Data", error.message)
            }

        })

        return listUser
    }
}