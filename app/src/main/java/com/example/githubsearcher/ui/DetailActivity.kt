package com.example.githubsearcher.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubsearcher.R
import com.example.githubsearcher.database.Favorite
import com.example.githubsearcher.databinding.ActivityDetailBinding
import com.example.githubsearcher.viewModel.DetailViewModel
import com.example.githubsearcher.viewModel.FavoriteViewModel
import com.example.githubsearcher.viewModel.FollowerViewModel
import com.example.githubsearcher.viewModel.FollowingViewModel
import com.example.githubsearcher.viewModel.factory.FavoriteViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favorite: Favorite
    private lateinit var users: String
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()
    private val followerViewModel by viewModels<FollowerViewModel>()
    private val followingViewModel by viewModels<FollowingViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        subscribe()
        fragment()
        binding.btnOpen.setOnClickListener(this)
        binding.btnShare.setOnClickListener(this)
        supportActionBar?.title = "Detail Profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        favoriteViewModel = obtainViewModel(this)

    }


    private fun fragment() {
        users = intent.getStringExtra(ApiAdapter.USER_KEY)!!
        val fragmentAdapter = FragmentAdapter(this, users)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = fragmentAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun subscribe() {
        val username = intent.getStringExtra(ApiAdapter.USER_KEY)
        if (username != null) {
            detailViewModel.detailApi(username)
            followingViewModel.getFollowing(username)
            followerViewModel.getFollower(username)
        }



        detailViewModel.setDetail.observe(this) {
            Glide.with(this).load(it.avatarUrl).into(binding.imgItemPhotoDetail)
            binding.namaOrangDetail.text = it.login
            if (it.name != null) {
                binding.idOrang.text = it.name
            } else {
                binding.idOrang.text = getString(R.string.unknown)
            }
            binding.follower.text = "${it.following} Followings - ${it.followers} Followers"
            if (it.location != null) {
                binding.kota.text = it.location
            } else {
                binding.kota.text = getString(R.string.unknown)
            }
            val id = intent.getIntExtra(EXTRA_ID, 0)

            if (it != null){
                favorite = Favorite(
                    id,
                    it.login!!,
                    it.avatarUrl,
                )
            }

            var checked = false
            CoroutineScope(Dispatchers.IO).launch{
                val count = favoriteViewModel.check(id)
                withContext(Dispatchers.Main){
                    if (count != null){
                        if(count > 1){
                            checked = true
                            binding.fabFav.isClickable = true
                            binding.fabFav.setImageResource(R.drawable.baseline_favorite_24)
                        }else{
                            checked = false
                            binding.fabFav.isClickable = true
                        }
                    }
                }
            }
            binding.fabFav.setOnClickListener {
                checked =! checked
                if (checked){
                    favoriteViewModel.insert(favorite)
                    binding.fabFav.setImageResource(R.drawable.baseline_favorite_24)
                    Toast.makeText(this, "${favorite.username} has been added to favorites", Toast.LENGTH_SHORT).show()
                }else {
                    favoriteViewModel.delete(favorite)
                    binding.fabFav.setImageResource(R.drawable.baseline_favorite_border_24)
                    Toast.makeText(this, "${favorite.username} has been removed to favorites", Toast.LENGTH_SHORT).show()
                }
            }
        }
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel{
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.memuat.visibility = View.VISIBLE
            binding.progressBars.visibility = View.VISIBLE
        } else {
            binding.memuat.visibility = View.GONE
            binding.progressBars.visibility = View.GONE
        }
    }

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.folling, R.string.follow
        )
        private const val EXTRA_ID = "extra_id"
        private const val BASE_URL = "https://github.com/"
    }

    override fun onClick(v: View?) {
        val user = intent.getStringExtra(ApiAdapter.USER_KEY)
        when (v?.id) {
            R.id.btnOpen -> {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW, Uri.parse("$BASE_URL${user}")
                    )
                )
            }

            R.id.btnShare -> {
                startActivity(Intent(Intent.ACTION_SEND).also {
                    it.putExtra(Intent.EXTRA_TEXT, "Link Profile : $BASE_URL${user}")
                    it.type = "text/plain"
                })
            }
        }
    }
}