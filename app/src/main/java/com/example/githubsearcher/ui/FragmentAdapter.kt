package com.example.githubsearcher.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(activity: AppCompatActivity, users: String) : FragmentStateAdapter(activity) {

    private var username: String = users

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = Follow()
        fragment.arguments = Bundle().apply {
            putInt(Follow.POSITION, position + 1)
            putString(Follow.USERNAME, username)
        }
        return fragment
    }

}