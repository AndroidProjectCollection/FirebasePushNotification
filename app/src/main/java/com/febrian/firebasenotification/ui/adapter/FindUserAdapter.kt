package com.febrian.firebasenotification.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.febrian.firebasenotification.data.User
import com.febrian.firebasenotification.databinding.ItemUserBinding
import com.febrian.firebasenotification.ui.activity.ChatActivity
import com.febrian.firebasenotification.utils.Constant

class FindUserAdapter(private val listUser : ArrayList<User>) : RecyclerView.Adapter<FindUserAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding : ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user : User){
            binding.apply {
                name.text = user.username
                email.text = user.email
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ChatActivity::class.java)
                intent.putExtra(Constant.USER, user)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindUserAdapter.ViewHolder {
        val view = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FindUserAdapter.ViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size
}