package com.febrian.firebasenotification.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.febrian.chat_sdk.viewmodel.UserViewModel
import com.febrian.chat_sdk.utils.Constant
import com.febrian.chat_sdk.utils.PreferenceManager
import com.febrian.chat_sdk.utils.ViewModelFactory
import com.febrian.firebasenotification.databinding.ActivityFindUserBinding
import com.febrian.firebasenotification.ui.adapter.FindUserAdapter

class FindUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFindUserBinding
    private lateinit var findUserAdapter: FindUserAdapter
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceManager = PreferenceManager(applicationContext)

        val uid = preferenceManager.getString(Constant.UID)
        val userViewModel = ViewModelFactory(this).create(UserViewModel::class.java)

        userViewModel.getFindUser(uid).observe(this){
            findUserAdapter = FindUserAdapter(it)
            binding.rvListUser.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = findUserAdapter
            }
        }

        userViewModel.isLoading.observe(this){
            when (it) {
                true -> {
                    binding.loading.visibility = View.VISIBLE
                }
                false -> {
                    binding.loading.visibility = View.GONE
                }
            }
        }

    }
}