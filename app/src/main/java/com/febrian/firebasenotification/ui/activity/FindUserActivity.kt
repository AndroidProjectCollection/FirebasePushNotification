package com.febrian.firebasenotification.ui.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.febrian.firebasenotification.data.User
import com.febrian.firebasenotification.databinding.ActivityFindUserBinding
import com.febrian.firebasenotification.ui.adapter.FindUserAdapter
import com.febrian.firebasenotification.utils.Constant
import com.google.firebase.database.*

class FindUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFindUserBinding
    private lateinit var database: DatabaseReference
    private lateinit var findUserAdapter: FindUserAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private var listUser = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences(Constant.sharedPreferences, Context.MODE_PRIVATE)
        database = FirebaseDatabase.getInstance().reference
        findUserAdapter = FindUserAdapter(listUser)

        val localUid = sharedPreferences.getString(Constant.UID, "").toString()

        binding.loading.visibility = View.VISIBLE
        database.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.loading.visibility = View.GONE

                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val uid = it.child("uid").value.toString()
                        if (localUid == uid) {
                            return@forEach
                        }
                        val name = it.child("username").value.toString()
                        val email = it.child("email").value.toString()
                        val password = it.child("password").value.toString()
                        val token = it.child("token").value.toString()

                        val user = User(uid, name, email, password, token)
                        listUser.add(user)
                        findUserAdapter.notifyDataSetChanged()

                    }

                } else {
                    Log.d("Data", "Nothing")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                binding.loading.visibility = View.GONE
                Log.d("Data", error.message)
            }

        })

        binding.rvListUser.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = findUserAdapter
        }
    }
}