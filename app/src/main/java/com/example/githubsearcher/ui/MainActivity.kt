package com.example.githubsearcher.ui

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubsearcher.databinding.ActivityMainBinding
import com.example.githubsearcher.response.ItemsItem
import com.example.githubsearcher.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribe()

        binding.searchView.setOnQueryTextListener(this)
        supportActionBar?.hide()
//        binding.toFav.setOnClickListener{
//            startActivity(Intent(this, FavoritActivity::class.java))
//        }
    }

    override fun onStart() {
        super.onStart()
    }


    private fun subscribe() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvHeroes.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvHeroes.addItemDecoration(itemDecoration)

        mainViewModel.apiData.observe(this) {
            setApiData(it)
        }
        mainViewModel.showDefault.observe(this) {
            binding.searchUser.isVisible = it
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        mainViewModel.showError.observe(this) {
            binding.tvErrorMessage.isVisible = it
        }
        mainViewModel.showFailed.observe(this) {
            binding.tvFailedMessage.isVisible = it
        }
        mainViewModel.snackBar.observe(this) {
            it.getContentIfNotHandled()?.let {
                Snackbar.make(
                    window.decorView.rootView,
                    "Ditemukan $it pengguna",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.memuat.visibility = View.VISIBLE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.memuat.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setApiData(listApi: List<ItemsItem>) {
        val adapter = ApiAdapter()
        adapter.submitList(listApi)
        binding.rvHeroes.adapter = adapter
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        mainViewModel.findApi(query)
        binding.searchView.clearFocus()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }
}