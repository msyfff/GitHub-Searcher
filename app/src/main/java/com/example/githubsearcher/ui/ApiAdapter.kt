package com.example.githubsearcher.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubsearcher.databinding.RowApiBinding
import com.example.githubsearcher.response.ItemsItem

class ApiAdapter : ListAdapter<ItemsItem, ApiAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: RowApiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ItemsItem) {
            Glide.with(itemView).load(review.avatarUrl).into(binding.imgItemPhoto)
            binding.namaOrang.text = review.login
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowApiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra(USER_KEY, review.login)
            holder.itemView.context.startActivity(intent)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean =
                oldItem == newItem


            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean =
                oldItem == newItem
        }
        const val USER_KEY = "login"
    }
}